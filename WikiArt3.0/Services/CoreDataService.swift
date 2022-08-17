
//
//  CoreDataService.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-29.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import CoreData

struct CoreDataService {
    
    static var service: NSPersistentContainer = {
        
        let container = NSPersistentContainer(name: "Model")
        
        container.loadPersistentStores
        { (description, error) in
            
        }
        print("Loaded")
        return container
    }()
    
}

//
//struct CoreDataService: ServiceType {
//
//    var appId: String
//
//    var serverConfig: ServerConfigType
//
//    var language: String
//
//    var currency: String
//
//    var buildVersion: String
//
//    func paintings(for category: PaintingCategory, page: Int, competion: @escaping PaintingListCompletion) {
//        <#code#>
//    }
//
//    func sections(for category: PaintingCategory, competion: @escaping PaintingSectionCompletion) {
//        <#code#>
//    }
//
//    func getPaintingDetails(for painting: Painting, competion: @escaping PaintingDetailsCompletion) {
//        <#code#>
//    }
//
//    func getRelatedPainting(for painting: Painting, competion: @escaping PaintingListCompletion) {
//        <#code#>
//    }
//
//    func getFamousPainting(for artist: Artist, competion: @escaping PaintingListCompletion) {
//        <#code#>
//    }
//
//    func getAllPainting(for artist: Artist, sort: ArtistPaintingListSort, page: Int, competion: @escaping PaintingListCompletion) {
//        <#code#>
//    }
//
//    func getArtistDetails(for path: String?, competion: @escaping ArtistDetailsCompletion) {
//        <#code#>
//    }
//
//    func getSections(for artistCategory: ArtistCategory, competion: @escaping ArtistSectionsCompletion) {
//        <#code#>
//    }
//
//    func getArtists(for artistCategory: ArtistCategory, in artistSection: ArtistsSection?, page: Int, competion: @escaping ArtistsListCompletion) {
//        <#code#>
//    }
//
//    func getPaintingSearchResults(for term: String, competion: @escaping PaintingListCompletion) {
//        <#code#>
//    }
//
//    func getArtistsSearchResults(for term: String, competion: @escaping ArtistsListCompletion) {
//        <#code#>
//    }
//
//    func getAutoCompleteSearchResults(for term: String, competion: @escaping AutoCompleteCompletion) {
//        <#code#>
//    }
//
//    func getPaintingsList(for type: SearchAutoComplete.ResultType?, page: Int, competion: @escaping PaintingListCompletion) {
//        <#code#>
//    }
//
//    func getArtistsList(for type: SearchAutoComplete.ResultType?, page: Int, competion: @escaping ArtistsListCompletion) {
//        <#code#>
//    }
//
//}
//
