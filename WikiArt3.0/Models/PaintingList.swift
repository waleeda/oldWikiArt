//
//  Painting.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-09.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

class PaintingList: Codable {
    var paintings: [Painting]
    let allPaintingsCount, pageSize: Int
    
    enum CodingKeys: String, CodingKey {
        case paintings = "Paintings"
        case allPaintingsCount = "AllPaintingsCount"
        case pageSize = "PageSize"
    }
    
    init(list: [Painting]) {
        self.pageSize = 1
        self.paintings = list
        self.allPaintingsCount = paintings.count
    }
    
}

extension PaintingList {
    
    var pageCount: Int {
        return (allPaintingsCount + pageSize - 1) / pageSize
    }
    
}
