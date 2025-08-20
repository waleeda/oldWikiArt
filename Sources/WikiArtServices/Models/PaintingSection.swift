//
//  PaintingSection.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-10.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

struct PaintingSection: Decodable {
    struct ID: Decodable {
        let _oid: String
    }
    
    struct ContentType: Decodable {
        struct TitleContent: Decodable {
            let Title: [String:String]
        }
        
        let Title: TitleContent
        let Status: Int
        let Group: Int
    }
    
    let _id: ID
    let Content: ContentType
    var title: String {
        get {
            let dict = self.Content.Title.Title
            return (dict[Current.language.rawValue] ?? dict[Language.en.rawValue]!)
        }
    }
}
