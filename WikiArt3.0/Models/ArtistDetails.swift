//
//  ArtistDetails.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-21.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

final class ArtistDetails: Codable {
    let relatedArtistsIDS: [String?]?
    let originalArtistName, gender: String?
    let story: String?
    let biography: String?
    let activeYearsCompletion, activeYearsStart: Int?
    let series, themes, periodsOfWork: String?
    let contentID: Int?
    let htmlArtistName, url, lastNameFirst, birthDay: String?
    let deathDay, birthDayAsString, deathDayAsString: String?
    let image: String?
    let wikipediaURL: String?
    let dictonaries: [Int?]?

    enum CodingKeys: String, CodingKey {
        case relatedArtistsIDS
        case originalArtistName
        case gender, story, biography, activeYearsCompletion, activeYearsStart, series, themes, periodsOfWork
        case contentID
        case htmlArtistName = "artistName", url, lastNameFirst, birthDay, deathDay, birthDayAsString, deathDayAsString, image
        case wikipediaURL
        case dictonaries
    }
    
    private var nativeArtistName: String?
    var artistName: String {
        if let convertedArtistName = nativeArtistName {
            return convertedArtistName
        }
        convertAndSetHTMLString(htmlArtistName ?? "" , &nativeArtistName)
        return nativeArtistName!
    }
    
}

struct ArtistDetailsDisplay: Codable {
    let gender: String?
    let biography: String?
    let series, themes, periods: String?
    let birth, death: String?

    init(details: ArtistDetails) {
        self.gender = details.gender?.capitalized
        self.biography = details.biography
        let newLine = "\r\n"
        let seperator =  ", "
        self.series = details.series?.replacingOccurrences(of: newLine, with: seperator)
        self.themes = details.themes?.replacingOccurrences(of: newLine, with: seperator)
        self.periods = details.periodsOfWork?.replacingOccurrences(of: newLine, with: seperator)
        self.birth = details.birthDayAsString
        self.death = details.deathDayAsString
    }
}

//let aa1 = NSLocalizedString("gender", comment: "")
//let a23 = NSLocalizedString("biography", comment: "")
//let a34 = NSLocalizedString("series", comment: "")
//let a45 = NSLocalizedString("themes", comment: "")
//let a56 = NSLocalizedString("periods", comment: "")
//let a67 = NSLocalizedString("birth", comment: "")
//let a6g7 = NSLocalizedString("death", comment: "")
