//
//  PaintingCategory.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-10.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

public enum PaintingCategory: Equatable {
    case media(id: String?)
    case style(id: String?)
    case genre(id: String?)
    case highRes
    //case random
    case popular
    case featured
    case favorites
}

public extension PaintingCategory {

    static let all: [PaintingCategory] = [.favorites , .featured, .popular, .media(id: nil), .style(id: nil), .genre(id: nil), .highRes]

    public var title: String {
        switch self {
        case .featured:
            return NSLocalizedString("Featured", comment: "")
        case .popular:
            return NSLocalizedString("Popular", comment: "")
        case .genre:
            return NSLocalizedString("Genre", comment: "")
        case .style:
            return NSLocalizedString("Style", comment: "")
        case .media:
            return NSLocalizedString("Media", comment: "")
        case .favorites:
            return NSLocalizedString("Favorites", comment: "")
        case .highRes:
            return NSLocalizedString("High Resolution", comment: "")
        }
    }
    
    #if canImport(UIKit)
    public var icon: Icon {
        switch self {
        case .featured:
            return .featured
        case .popular:
            return .popular
        case .genre:
            return .genre
        case .style:
            return .style
        case .media:
            return .media
        case .favorites:
            return .favorites
        case .highRes:
            return .highres
        }
    }
    #endif
    
    public var hasSections: Bool {
        switch self {
        case .featured:
            return false
        case .popular:
            return false
        case .genre:
            return true
        case .style:
            return true
        case .media:
            return true
        case .favorites:
            return false
        case .highRes:
            return false
        }
    }
}
