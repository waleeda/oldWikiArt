//
//  PaintingDetails.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-21.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

// MARK: - PaintingDetails
struct PaintingDetails: Codable {
    let id, title, url, artistURL: String?
    let artistName, artistID: String?
    let completitionYear: Int?
    let dictionaries: [String]?
    let location: String?
    let period, serie: String?
    let genres, styles, media: [String]?
    let sizeX, sizeY: Float?
    let diameter: Double?
    let galleries, tags: [String]?
    let description: String?
    let width: Int?
    let image: String?
    let height: Int?

    enum CodingKeys: String, CodingKey {
        case id, title, url
        case artistURL
        case artistName
        case artistID
        case completitionYear, dictionaries, location, period, serie, genres, styles, media, sizeX, sizeY, diameter, galleries, tags
        case description
        case width, image, height
    }
}

struct PaintingDetailsDisplay: Codable {
    //let date: Int?
    let location: String?
    let period, series: String?
    let genres, styles, media: [String]?
    let diameter: Double?
    let galleries, tags: [String]?
    let description: String?
    let dimensions: String?
    
    init(details: PaintingDetails) {
       // self.date = details.completitionYear
        self.location = details.location?.capitalized
        self.period = details.period?.capitalized
        self.series = details.serie?.capitalized
        self.genres = details.genres?.map { $0.capitalized }
        self.styles = details.styles?.map { $0.capitalized }
        self.media = details.media?.map { $0.capitalized }
        self.diameter = details.diameter
        self.galleries = details.galleries?.map { $0.capitalized }
        self.tags = details.tags?.map { $0.capitalized }
        self.description = details.description
        if let y = details.sizeY, let x = details.sizeX {
            self.dimensions = "\(x) x \(y) CM"
        }
        else {
            self.dimensions = nil
        }
    }
}

//let aa = NSLocalizedString("date", comment: "")
//let a1 = NSLocalizedString("location", comment: "")
//let a2 = NSLocalizedString("period", comment: "")
//let a3 = NSLocalizedString("series", comment: "")
//let a4 = NSLocalizedString("genres", comment: "")
//let a5 = NSLocalizedString("styles", comment: "")
//let a6 = NSLocalizedString("media", comment: "")
//let a7 = NSLocalizedString("diameter", comment: "")
//let a9 = NSLocalizedString("galleries", comment: "")
//let a12 = NSLocalizedString("tags", comment: "")
//let a33 = NSLocalizedString("description", comment: "")
//let a44 = NSLocalizedString("dimensions", comment: "")
