//
//  ArtistsList.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-22.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

// MARK: - ArtistsList
struct ArtistsList: Codable {
    let artistsHTML: String?
    let canLoadMoreArtists: Bool
    let paintings: [Painting]?
    let artists: [Artist]
    let allArtistsCount: Int
    let paintingsHTML, paintingsHTMLBeta: String?
    let allPaintingsCount: Int?
    let pageSize: Int?
    let timeLog: String?

    enum CodingKeys: String, CodingKey {
        case artistsHTML
        case canLoadMoreArtists = "CanLoadMoreArtists"
        case paintings
        case artists = "Artists"
        case allArtistsCount = "AllArtistsCount"
        case paintingsHTML
        case paintingsHTMLBeta
        case allPaintingsCount
        case pageSize = "PageSize"
        case timeLog
    }
}
