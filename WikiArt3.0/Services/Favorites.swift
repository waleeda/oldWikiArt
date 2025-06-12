//
//  FavoritesManager.swift
//  WikiArt2.0
//
//  Created by Waleed Azhar on 2018-09-13.
//  Copyright © 2018 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit

protocol FavoritesDelegate: class {
    func didChange()
}

final class Favorites {
    
    static var paintingsKeyLegacy = "fav_paintings"
    static var paintingsKey = "com.littlemarvel.wikiart"
    static var artistsKey = "fav_paintings"
    private var paintings: Set<Painting> = []
    weak var delegate:FavoritesDelegate?
}

extension Favorites {
 
    public func contains(_ a: Painting) -> Bool {
        return paintings.contains(a)
    }
    
    public func append(painting p: Painting) {
        if !paintings.contains(p) {
            paintings.insert(p)
            self.save()
            
        }
    }
    
    public func remove(painting p: Painting) {
        paintings.remove(p)
        self.save()
    }
    
}

extension Favorites {

    public var paintingsSorted: [Painting ] {
        return paintings.sorted() { (a, b) -> Bool in
            return a.title.caseInsensitiveCompare( b.title )  == ComparisonResult.orderedAscending
        }
    }
    
}

extension Favorites {
    
    public func save() {
        let encoder = JSONEncoder()
        if let data = try? encoder.encode(paintingsSorted) {
            UserDefaults.standard.set(data, forKey: Favorites.paintingsKey )
            delegate?.didChange()
        }
    }
    
    public func restoreLegacy() {
        let decoder = JSONDecoder()
        guard let data = UserDefaults.standard.value(forKey: Favorites.paintingsKeyLegacy) as? Data else { return }
        if let ps = try? decoder.decode([WAPainting].self, from: data) {
            paintings = paintings.union( ps.map { Painting($0) } )
        }
        UserDefaults.standard.set(nil, forKey: Favorites.paintingsKeyLegacy)
    }
    
    public func restore() {
        let decoder = JSONDecoder()
        guard let data = UserDefaults.standard.value(forKey: Favorites.paintingsKey) as? Data else { return }
        if let ps = try? decoder.decode( [Painting].self, from: data)
        { paintings = paintings.union(ps) }
    }
    
}


class WAPainting: Codable {
  
    var title: String = ""
    var artistName: String = ""
    var id: String = ""
    var year: String = ""
    var width: CGFloat = 0.0
    var height: CGFloat = 0.0
    var image: String = ""
    var map: String = ""
    var paintingUrl: String = ""
    var flags: Int = 0
    var imageUI:UIImage? = nil

    
    enum CodingKeys:String, CodingKey {
        case id
        case title
        case year
        case width
        case height
        case artistName
        case image
        case map
        case paintingUrl
        case flags
    }
    
}
