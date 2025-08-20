//
//  Service.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-02.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

public protocol LoggerType {
    func log(_ message: String)
}

public protocol FavoritesProviding {
    var paintingsSorted: [Painting] { get }
}

public struct Service: ServiceType {

    public var appId: String

    public var serverConfig: ServerConfigType

    public var language: String

    public var currency: String

    public var buildVersion: String

    public var favoritesProvider: FavoritesProviding?
    public var logger: LoggerType?

    public init(appId: String = Bundle.main.bundleIdentifier ?? "com.littlemarvel.WikiArt",
                serverConfig: ServerConfigType = ServerConfig.production,
                language: String = "en",
                currency: String = "CAD",
                buildVersion: String = Bundle.main._buildVersion,
                favoritesProvider: FavoritesProviding? = nil,
                logger: LoggerType? = nil) {

        self.appId = appId
        self.serverConfig = serverConfig
        self.language = language
        self.currency = currency
        self.buildVersion = buildVersion
        self.favoritesProvider = favoritesProvider
        self.logger = logger
        Service.session.configuration.requestCachePolicy = .returnCacheDataElseLoad
    }
    
    public func paintings(for category: PaintingCategory,
                          page: Int,
                          competion: @escaping PaintingListCompletion) {
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
            if let favs = favoritesProvider?.paintingsSorted {
                competion(.success(PaintingList(list: favs)))
            } else {
                competion(.failure(BadServiceError()))
            }
        }
    }
    
    public func getPaintingDetails(for painting: Painting, competion: @escaping PaintingDetailsCompletion) {
        request(.paintingDetails(id: painting.id), competion)
    }
    
    public func getRelatedPainting(for painting: Painting, competion: @escaping PaintingListCompletion) {
        request(.relatedPainting(path: painting.paintingURL), competion)
    }
    
    public func getFamousPainting(for artist: Artist, competion: @escaping PaintingListCompletion) {
        request(.famousPaintings(path: artist.artistURL ?? ""), competion)
    }
    
    public func getAllPainting(for artist: Artist, sort: ArtistPaintingListSort, page: Int, competion: @escaping PaintingListCompletion) {
        switch sort {
        case .name:
            request(.allPaintingsByAlphabet(path: artist.artistURL ?? "", page: page), competion)
        case .date:
            request(.allPaintingsByDate(path: artist.artistURL ?? "", page: page), competion)
        }
    }
    
    public func getArtistDetails(for path: String?, competion: @escaping ArtistDetailsCompletion) {
        request(.artistDetails(path: path ?? ""), competion)
    }
    
    public func getSections(for artistCategory: ArtistCategory, competion: @escaping ArtistSectionsCompletion) {
        request(.artistSectionsFor(categoryPath: artistCategory.rawValue), competion)
    }
    
    public func getArtists(for artistCategory: ArtistCategory, in artistSection: ArtistsSection?, page: Int, competion: @escaping ArtistsListCompletion) {
        request(.artistsFor(cateory: artistCategory.rawValue, section: artistSection?.url, page: page), competion)
    }
    
    public func sections(for category: PaintingCategory, competion: @escaping PaintingSectionCompletion) {
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
    
    public func getPaintingSearchResults(for term: String, competion: @escaping PaintingListCompletion) {
        request(.searchForPainting(term), competion)
    }
    
    public func getArtistsSearchResults(for term: String, competion: @escaping ArtistsListCompletion) {
        request(.searchForArtist(term), competion)
    }
    
    public func getAutoCompleteSearchResults(for term: String, competion: @escaping AutoCompleteCompletion) {
        request(.searchAutoComplete(term: term), competion)
    }
    
    public func getPaintingsList(for type: SearchAutoComplete.ResultType?, page: Int, competion: @escaping PaintingListCompletion) {
        switch type {
        case .paintingsCategory(let path):
            request(.paintingsList(path: path, page: page), competion)
        default:
            competion(.failure(BadServiceError()))
        }
        
    }
    
    public func getArtistsList(for type: SearchAutoComplete.ResultType?, page: Int, competion: @escaping ArtistsListCompletion) {
        switch type {
        case .artistCategory(let path):
            request(.artistsList(path: path, page: page), competion)
        default:
            competion(.failure(BadServiceError()))
        }
    }

}

public extension Service {

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
        let properties = route.requestProperties(languageCode: self.language)
       
        guard let url = URL(string: properties.path, relativeTo: self.serverConfig.apiBaseUrl) else {
            print( "URL(string: \(properties.path), relativeToURL: \(self.serverConfig.apiBaseUrl)) == nil" )
            completion(.failure(DataTaskFailure()))
            return
        }
        
        //TODO: Need to add queries
        let request = preparedRequest(forURL: url, method: properties.method, query: properties.query)
        logger?.log(request.url?.absoluteString ?? "No Url in request")
        let dataTask = Service.session.dataTask(with: request ) { data, response, error in
            
            guard let response = response as? HTTPURLResponse else {
                logger?.log("[Api] Failure: No Response")
                return completion(.failure(DataTaskFailure()))
            }
            
            guard
                (200..<300).contains(response.statusCode),
                let data = data
                else {
                    logger?.log("[Api] Failure \(response.url!)")
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
