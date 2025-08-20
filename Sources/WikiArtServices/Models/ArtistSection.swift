//
//  ArtistSectionsFetcher.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-22.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

struct ID: Decodable {
    let _oid: String
}

struct ArtistsSection: Decodable {
    let CategoryId: ID
    let Url: String
    let Title: String
    let Count: Int
    
    var url: String {
        return String(Url.split(separator: Character(unicodeScalarLiteral: "/")).last ?? "")
    }
}

struct ArtistSections: Decodable {

    struct ArtistTypesKey: Decodable {
        struct ContentType: Decodable {
            struct TitleContent: Decodable {
                let Title: [String:String]
            }
            
            let Title: TitleContent
        }
        
        let _id: ID
        let Content: ContentType
    }
    
    let Categories:[ArtistTypesKey]
    let DictionariesWithCategories: [String: [ArtistsSection]]
    let Group: Int
    let Menu: String?
    let SortBy: Int
    
    var items: [ArtistsSection] {
        get { return DictionariesWithCategories.values.flatMap { return $0 } }
    }
}
