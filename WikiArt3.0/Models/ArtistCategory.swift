//
//  ArtistCategories.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-22.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

enum ArtistCategory: String {
    static let all: [ArtistCategory] =  [ .popular, .female, .recent, .artMovement, .school, .genre, .field, .nation, .centuries, .chronology, .institutions]
    static let categoriesWith: [ArtistCategory] = [.artMovement, .school, .genre, .field, .nation, .centuries, .institutions]
    
    case alphabetical = "Alphabet"
    case artMovement = "artists-by-Art-Movement"
    case school = "artists-by-painting-school"
    case genre = "artists-by-genre"
    case field = "artists-by-field"
    case nation = "artists-by-nation"
    case centuries = "artists-by-century"
    case chronology = "chronological-artists"
    case popular = "popular-artists"
    case female = "female-artists"
    case recent = "recently-added-artists"
    case institutions = "artists-by-art-institution"
}

extension ArtistCategory {

    var title: String {
        switch self {
        case .alphabetical:
            return NSLocalizedString("Alphabet", comment: "")
        case .artMovement:
            return NSLocalizedString("Art Movement", comment: "")
        case .school:
            return NSLocalizedString("Painting School", comment: "")
        case .genre:
            return NSLocalizedString("Genre", comment: "")
        case .field:
            return NSLocalizedString("Field", comment: "")
        case .nation:
            return NSLocalizedString("Nation", comment: "")
        case .centuries:
            return NSLocalizedString("Century", comment: "")
        case .chronology:
            return NSLocalizedString("Chronological", comment: "")
        case .popular:
            return NSLocalizedString("Popular", comment: "")
        case .female:
            return NSLocalizedString("Female", comment: "")
        case .recent:
            return NSLocalizedString("Recent", comment: "")
        case .institutions:
            return NSLocalizedString("Art Institution", comment: "")
        }
    }
    
    var hasSections: Bool {
        switch self {
        case .alphabetical:
            return false
        case .artMovement:
            return true
        case .school:
            return true
        case .genre:
            return true
        case .field:
            return true
        case .nation:
            return true
        case .centuries:
            return true
        case .chronology:
            return false
        case .popular:
            return false
        case .female:
            return false
        case .recent:
            return false
        case .institutions:
            return true
        }
    }
    
    var icon: Icon {
        switch self {
        case .popular:
            return .popular
        case .genre:
            return .genre
        case .alphabetical:
            return .blank
        case .artMovement:
            return .style
        case .school:
            return .school
        case .field:
            return .field
        case .nation:
            return .nation
        case .centuries:
            return .century
        case .chronology:
            return .chronological
        case .female:
            return .female
        case .recent:
            return .recent
        case .institutions:
            return .institution
        }
    }
   
}


