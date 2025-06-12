//
//  Service.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-02.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

struct Service: ServiceType {
    
    var appId: String
    
    var serverConfig: ServerConfigType
    
    var language: String
    
    var currency: String
    
    var buildVersion: String
    
    public init(appId: String = Bundle.main.bundleIdentifier ?? "com.littlemarvel.WikiArt",
                serverConfig: ServerConfigType = ServerConfig.production,
                language: String = "en",
                currency: String = "CAD",
                buildVersion: String = Bundle.main._buildVersion) {
        
        self.appId = appId
        self.serverConfig = serverConfig
        self.language = language
        self.currency = currency
        self.buildVersion = buildVersion
        Service.session.configuration.requestCachePolicy = .returnCacheDataElseLoad
    }
    
    func paintings(for category: PaintingCategory,
                   page: Int,
                   competion: @escaping PaintingListCompletion)
    {
        switch category {
        case .featured:
            request(.featured(page: page), competion)
        case .popular:
            request(.popular(page: page), competion)
        case .genre(let id):
            request(.section(id: id, page: page), competion)
        case .media(let id):
            request(.section(id: id, page: page), competion)
        case .style(let id):
            request(.section(id: id, page: page), competion)
        case .highRes:
            request(.highRes(page: page), competion)
        case .favorites:
            competion(.success(PaintingList(list: Current.favouritesService.paintingsSorted)))
        }
    }
    
    func getPaintingDetails(for painting: Painting, competion: @escaping PaintingDetailsCompletion) {
        request(.paintingDetails(id: painting.id), competion)
    }
    
    func getRelatedPainting(for painting: Painting, competion: @escaping PaintingListCompletion) {
        request(.relatedPainting(path: painting.paintingURL), competion)
    }
    
    func getFamousPainting(for artist: Artist, competion: @escaping PaintingListCompletion) {
        request(.famousPaintings(path: artist.artistURL ?? ""), competion)
    }
    
    func getAllPainting(for artist: Artist, sort:ArtistPaintingListSort, page: Int, competion: @escaping PaintingListCompletion) {
        switch sort {
        case .name:
            request(.allPaintingsByAlphabet(path: artist.artistURL ?? "", page: page), competion)
        case .date:
            request(.allPaintingsByDate(path: artist.artistURL ?? "", page: page), competion)
        }
    }
    
    func getArtistDetails(for path: String?, competion: @escaping ArtistDetailsCompletion) {
        request(.artistDetails(path: path ?? ""), competion)
    }
    
    func getSections(for artistCategory: ArtistCategory, competion: @escaping ArtistSectionsCompletion) {
        request(.artistSectionsFor(categoryPath: artistCategory.rawValue), competion)
    }
    
    func getArtists(for artistCategory: ArtistCategory, in artistSection: ArtistsSection?, page: Int, competion: @escaping ArtistsListCompletion) {
        request(.artistsFor(cateory: artistCategory.rawValue, section: artistSection?.url, page: page), competion)
    }
    
    func sections(for category: PaintingCategory, competion: @escaping PaintingSectionCompletion) {
        switch category {
        case .genre:
            request(.genreSections, competion)
        case .media:
            request(.mediaSections, competion)
        case .style:
            request(.styleSections, competion)
        default:
           competion(.failure(NoSectionsForCategory()))
        }
    }
    
    func getPaintingSearchResults(for term: String, competion: @escaping PaintingListCompletion) {
        request(.searchForPainting(term), competion)
    }
    
    func getArtistsSearchResults(for term: String, competion: @escaping ArtistsListCompletion) {
        request(.searchForArtist(term), competion)
    }
    
    func getAutoCompleteSearchResults(for term: String, competion: @escaping AutoCompleteCompletion) {
        request(.searchAutoComplete(term: term), competion)
    }
    
    func getPaintingsList(for type: SearchAutoComplete.ResultType?, page: Int, competion: @escaping PaintingListCompletion) {
        switch type {
        case .paintingsCategory(let path):
            request(.paintingsList(path: path, page: page), competion)
        default:
            competion(.failure(BadServiceError()))
        }
        
    }
    
    func getArtistsList(for type: SearchAutoComplete.ResultType?, page: Int, competion: @escaping ArtistsListCompletion) {
        switch type {
        case .artistCategory(let path):
            request(.artistsList(path: path, page: page), competion)
        default:
            competion(.failure(BadServiceError()))
        }
    }

}

extension Service {
    
    static let session: URLSession = {
        let configuration = URLSessionConfiguration.default
        configuration.timeoutIntervalForRequest = 30
        configuration.timeoutIntervalForResource = 30
        configuration.requestCachePolicy = .returnCacheDataElseLoad
        let session = URLSession(configuration: configuration)
        configuration.urlCache?.removeAllCachedResponses()
        return session
    }()
    
    private func request<A: Decodable>(_ route: Route, _ completion: @escaping (Result<A, Error>) -> Void ) {
        let properties = route.requestProperties
       
        guard let url = URL(string: properties.path, relativeTo: self.serverConfig.apiBaseUrl) else {
            print( "URL(string: \(properties.path), relativeToURL: \(self.serverConfig.apiBaseUrl)) == nil" )
            completion(.failure(DataTaskFailure()))
            return
        }
        
        //TODO: Need to add queries
        let request = preparedRequest(forURL: url, method: properties.method, query: properties.query)
        Current.logger.log(request.url?.absoluteString ?? "No Url in request")
        let dataTask = Service.session.dataTask(with: request ) { data, response, error in
            
            guard let response = response as? HTTPURLResponse else {
                Current.logger.log("[Api] Failure: No Response")
                return completion(.failure(DataTaskFailure()))
            }
            
            guard
                (200..<300).contains(response.statusCode),
                let data = data
                else {
                    Current.logger.log("[Api] Failure \(response.url!)")
                    let resultError = error ?? DataTaskFailure()
                    return completion( .failure(resultError) )
            }
            let jsonDecoder = JSONDecoder()
            if let model = try? jsonDecoder.decode(A.self, from: data) {
                return completion(.success(model))
                
            } else {
                return completion(.failure(JSONDecodeFailure()) )
            }
        }
        
        dataTask.resume()
    }
    
}
