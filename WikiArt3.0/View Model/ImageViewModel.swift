//
//  ImageViewModel.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-11.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

class ImageViewModel {
    let image: ImagePath
    let placeholder: ImagePath?
    let width: CGFloat
    let height: CGFloat
    var imageUse: UIImage?
    var placeHolderImage: UIImage?
    
    init(_ path: ImagePath, _ placeholder: ImagePath? = nil, _ width: Double, _ height: Double) {
        self.image = path
        self.placeholder = placeholder
        self.width = CGFloat(width)
        self.height = CGFloat(height)
        self.imageUse = Current.imageServie.cachedImaged(from: path)
        self.placeHolderImage = Current.imageServie.cachedImaged(from: placeholder ?? "")        
    }
    
    convenience init(_ painting: Painting,
                     paintingQulity: Painting.Size = Current.defaultPaintingSize,
                     placeHolderQuality: Painting.Size = Current.defaultPlaceholderPaintingSize) {
        
        self.init(painting.image(size: paintingQulity),
                  painting.image(size: placeHolderQuality),
                  Double(painting.width),
                  Double(painting.height))
    }
    
    convenience init(_ artist: Artist) {
        self.init(artist.image ?? "", "BlankArtist", 0.0, 0.0)
    }
    
    init(_ auto: SearchAutoComplete) {
        self.image = auto.image ?? ""
        self.placeholder = ""
        self.height = 0.0
        self.width = 0.0
    }
}

