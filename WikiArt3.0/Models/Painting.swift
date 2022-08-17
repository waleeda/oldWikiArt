//
//  Painting.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-09.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
typealias ImagePath = String

class Painting: Codable, Hashable {
    
    var hashValue: Int { id.hashValue }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
    static func == (lhs: Painting, rhs: Painting) -> Bool { lhs.id == rhs.id }
    
    enum Size:String {
        case blog = "!Blog.jpg"
        case large = "!Large.jpg"
        case smallPortrait = "!PortraitSmall.jpg"
        case portrait = "!Portrait.jpg"
        case halfHD = "!HalfHD.jpg"
        case HD = "!HD.jpg"
        case small = "!PinterestSmall.jpg"
        case medium = "!PinterestLarge.jpg"
        case original = ""
    }
    
    let id, htmlTitle, year: String
    let width, height: Int
    let htmlArtistName: String
    let image: ImagePath
    let paintingURL, artistURL: String
    let flags: Int
    
    private var nativeTitle: String?
    
    var title: String {
        if let convertedTitle = nativeTitle {
            return convertedTitle
        }
        convertAndSetHTMLString(htmlTitle, &nativeTitle)
        return nativeTitle!
    }
    
    private var nativeArtistName: String?
    var artistName: String {
        if let convertedArtistName = nativeArtistName {
            return convertedArtistName
        }
        convertAndSetHTMLString(htmlArtistName, &nativeArtistName)
        return nativeArtistName!
    }
    
    
    var creationYear: Int {
        return Int(year) ?? 1900
    }
    
    enum CodingKeys: String, CodingKey {
        case id, year, width, height, image
        case htmlArtistName = "artistName"
        case paintingURL = "paintingUrl"
        case htmlTitle = "title"
        case artistURL = "artistUrl"
        case flags
    }

    func image(size: Size) -> ImagePath {
        return image + size.rawValue
    }
    
    init(_ painting: WAPainting) {
        self.id = painting.id
        self.htmlTitle = painting.title
        self.year = painting.year
        self.width = Int(painting.width)
        self.height = Int(painting.height)
        self.htmlArtistName = painting.artistName
        self.image = painting.image
        self.paintingURL = painting.paintingUrl
        self.flags = painting.flags
        self.artistURL = ""
    }

}

func convertAndSetHTMLString(_ htmlString: String, _ native: inout String?) {
    native = htmlString.hasUnicode ? htmlString.html2String : htmlString
}
