//
//  Artist.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-22.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

// MARK: - Artist
struct Artist: Codable {
    let id, title, year: String?
    let nation: String?
    let image: String?
    let artistURL, totalWorksTitle: String?

    enum CodingKeys: String, CodingKey {
        case id, title, year, nation, image
        case artistURL = "artistUrl"
        case totalWorksTitle
    }
    
    init(id: String?,
         title: String?,
         year: String?,
         nation: String?,
         image: String?,
         artistURL: String?,
         totalWorksTitle: String?)
    {
        self.id = id
        self.title = title
        self.year = year
        self.nation = nation
        self.image = image
        self.artistURL = artistURL
        self.totalWorksTitle = totalWorksTitle
    }
    
    init(details: ArtistDetails) {
        self.init(id: nil,
                  title: details.artistName,
                  year: details.birthDayAsString,
                  nation: nil,
                  image: details.image,
                  artistURL: details.url,
                  totalWorksTitle: nil)
    }
}

