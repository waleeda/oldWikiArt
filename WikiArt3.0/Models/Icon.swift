//
//  Icon.swift
//  test2
//
//  Created by Waleed Azhar on 2019-10-22.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

enum Icon: String {
    
    case century, chronological, favorites, featured, female, genre, institution, media, nation, popular, recent, school, section, style, highres, blank, title, sheetLayout, columnLayout, listLayout, field, support
    
    var image: UIImage? { UIImage(named: rawValue) }
    
    static let all = [century, chronological, favorites, featured, female, genre, institution, media, nation, popular, recent, school, section, blank, style, highres, title, sheetLayout, columnLayout, listLayout]
    

    static let layouts = [listLayout, sheetLayout, columnLayout].map { resizeImage(image: $0.image!, targetSize: .init(width: 16, height: 16))  }
    
    @available(iOS 13.0, *)
    static let systemNameLayouts = [UIImage(systemName: "square.split.1x2")!,
                                    UIImage(systemName: "rectangle.split.3x3")!,
                                    UIImage(systemName: "square.split.2x1")!]
}


 func resizeImage(image: UIImage, targetSize: CGSize) -> UIImage {
    let size = image.size

    let widthRatio  = targetSize.width  / size.width
    let heightRatio = targetSize.height / size.height

    // Figure out what our orientation is, and use that to form the rectangle
    var newSize: CGSize
    if(widthRatio > heightRatio) {
        newSize = CGSize(width: size.width * heightRatio, height: size.height * heightRatio)
    } else {
        newSize = CGSize(width: size.width * widthRatio,  height: size.height * widthRatio)
    }

    // This is the rect that we've calculated out and this is what is actually used below
    let rect = CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height)

    // Actually do the resizing to the rect using the ImageContext stuff
    UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
    image.draw(in: rect)
    let newImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()

    return newImage!
}
