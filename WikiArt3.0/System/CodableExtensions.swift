//
//  CodableExtensions.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-21.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

extension Encodable {
    var displayDict: [(String, String)] {
        var result: [(String, String)]  = []
        let dict = dictionary.sorted { (l, r) -> Bool in l.key <= r.key }
        dict.forEach {
            if let value = $1 as? String, value != "" {
               result += [($0, value)]
           }
            else if let value = $1 as? [String], !value.isEmpty {
                result += [($0, String(value))]
           }
           else if let value = $1 as? Int {
                result += [($0, value.description)]
           }
           else if let value = $1 as? Double {
                result += [($0, value.description)]
            }
            else if let value = $1 as? Float {
                result += [($0, value.description)]
            }
        }
           
        return result
    }
}


