//
//  SearchAutoComplete.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-25.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

// MARK: - WelcomeElement
struct SearchAutoComplete: Codable {
    let url, value, label, description: String
    let image: String?

    enum CodingKeys: String, CodingKey {
        case url = "Url"
        case value = "Value"
        case label = "Label"
        case description = "Description"
        case image = "Image"
    }
}

extension SearchAutoComplete {
    
    var type: ResultType? {
        return ResultType(self)
    }
    
    enum ResultType {
        
        case painting(path: String)
        case artist(path: String)
        case paintingsCategory(path: String)
        case artistCategory(path: String)
        case search(term: String)
        
        init?(_ auto: SearchAutoComplete) {
            if let type = ResultType(auto.url) {
                self = type
            }
            else {
                return nil
            }
        }
        
        init?(_ url: String) {
            let parts = url.split(separator: "/").map { String($0) }
            guard parts.count >= 2 else { return  nil }
            switch (parts[1], parts.count) {
            case ("paintings-by-style", 3):
                self = .paintingsCategory(path: url)
            case ("paintings-by-genre", 3):
                self = .paintingsCategory(path: url)
            case ("artists-by-art-movement", 3):
                self = .artistCategory(path: url)
            case ("artists-by-painting-school", 3):
                self = .artistCategory(path: url)
            case ("artists-by-nation", 3):
                self = .artistCategory(path: url)
            case ("Search", 3):
                self = .search(term: parts[2])
            case (_ , 2):
                self = .artist(path: url)
            case (_ , 3):
                self = .painting(path: url)
            default:
                return nil
            }
        }
    }
    
}
