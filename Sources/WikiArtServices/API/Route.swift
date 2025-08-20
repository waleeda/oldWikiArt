//
//  Route.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-02.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

public enum Route {
    case featured(page: Int)
    case popular(page: Int)
    case section(id: String?, page:Int)
    case highRes(page: Int)
    case paintingDetails(id: String?)
    case relatedPainting(path: String)
    
    case famousPaintings(path: String   )
    case allPaintingsByDate(path: String, page: Int)
    case allPaintingsByAlphabet(path: String, page: Int)
    
    case artistDetails(path: String)
    case artistSectionsFor(categoryPath: String)
    case artistsFor(cateory: String, section: String?, page: Int)
    
    case styleSections
    case genreSections
    case mediaSections
    
    case searchForPainting(String)
    case searchForArtist(String)
    case searchAutoComplete(term: String)
    
    case paintingsList(path: String, page: Int)
    case artistsList(path: String, page: Int)
    //case content

    internal func requestProperties(languageCode: String) -> (method: Method, path: String, query: [String: Any]) {
        switch self {
        case .highRes(let page):
            let params: [String: Any] = ["json": 2, "param": "high_resolution", "page": page]
            return (.GET, "/" + languageCode, params)
        case .featured(let page):
            let params: [String: Any] = ["json": 2, "param": "featured", "page": page]
            return (.GET, "/" + languageCode, params)
        case .popular(let page):
            let params: [String: Any] = ["page": page, "json": 2]
            return (.GET, "/" + languageCode + "/popular-paintings/alltime", params)
        case .section(let id, let page):
            let id = "[\"\(id ?? "")\"]"
            let params: [String: Any] = ["page": page, "json": 2, "dictIdsJson": id]
            return (.GET, "/" + languageCode + "/app/Search/PaintingAdvancedSearch", params)
        case .paintingDetails(let id):
            let params: [String: Any] = ["id": id ?? ""]
            return (.GET, "/" + languageCode + "/api/2/Painting", params)
        case .relatedPainting(let path):
            let params: [String: Any] = ["json": 2]
            return (.GET, path, params)

        case .famousPaintings(let path):
            let params: [String: Any] = ["json": 2, "page": 1]
            return (.GET, path + "/mode/featured", params)
        case let .allPaintingsByDate(path, page):
            let params: [String: Any] = ["json": 2, "page": page]
            return (.GET, path + "/all-works", params)
        case let .allPaintingsByAlphabet(path, page):
            let params: [String: Any] = ["json": 2, "page": page]
            return (.GET, path + "//mode/all-paintings-by-alphabet", params)

        case .artistDetails(let path):
            let params: [String: Any] = ["json": 2]
            return (.GET, path, params)
        case .artistSectionsFor(let path):
            let params: [String: Any] = ["json": 2]
            return (.GET, "/" + languageCode + "/App/Search/" + path, params)
        case let .artistsFor(category, section, page):
            let params: [String: Any] = ["json": 3, "layout": "new", "page": page, "searchterm": section ?? ""]
            return (.GET, "/" + languageCode + "/App/Search/" + category, params)

        case .styleSections:
            let params: [String: Any] = ["group": 2]
            return (.GET, "/" + languageCode + "/app/dictionary/GetAllGroup", params)
        case .genreSections:
            let params: [String: Any] = ["group": 3]
            return (.GET, "/" + languageCode + "/app/dictionary/GetAllGroup", params)
        case .mediaSections:
            let params: [String: Any] = ["group": 12]
            return (.GET, "/" + languageCode + "/app/dictionary/GetAllGroup", params)

        case .searchForPainting(let searchTerm):
            let cleanedSearchTerm = searchTerm.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
            let params: [String: Any] = ["pageSize": 5000, "json": 2]
            return (.GET, "/" + languageCode + "/Search/" + cleanedSearchTerm, params)
        case .searchForArtist(let searchTerm):
            let cleanedSearchTerm = searchTerm.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
            let params: [String: Any] = ["pageSize": 5000, "json": 3, "layout": "new", "page": 1, "resultType": "masonry"]
            return (.GET, "/" + languageCode + "/Search/" + cleanedSearchTerm, params)
        case .searchAutoComplete(let searchTerm):
            let params: [String: Any] = ["term": searchTerm]
            return (.GET, "/" + languageCode + "/app/search/autocomplete/", params)

        case let .paintingsList(path, page):
            let params: [String: Any] = ["json": 2, "page": page]
            return (.GET, path, params)
        case let .artistsList(path, page):
            let params: [String: Any] = ["json": 3, "page": page, "layout": "new"]
            return (.GET, path, params)
        }
    }
}


//paintingList
//artowrks list layout=new
