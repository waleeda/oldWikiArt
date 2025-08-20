//
//  PagedData.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-09.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

struct PagedResult<A: Codable> {
    let data: [A]
    let paginationToken: String?
    let hasMore: Bool
}
