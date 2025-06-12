//
//  Style.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-01.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit

struct AppStyle {
    struct Animation {
        static let transtionDuration = TimeInterval(exactly: 0.4)!
    }
    struct Font {
      

    }
    
    struct Layout {
        
    }
    
    struct Theme {
        
    }
    
    struct Color {
        static let jaguar = UIColor(red:0.16, green:0.16, blue:0.19, alpha:1.00)
        static let scampi = UIColor(red:0.44, green:0.35, blue:0.63, alpha:1.00)
        static let casper = UIColor(red:0.68, green:0.71, blue:0.73, alpha:1.00)
        static let riverBed = UIColor(red:0.25, green:0.29, blue:0.35, alpha:1.00)
        static let zicron = UIColor(red:0.87, green:0.90, blue:0.91, alpha:1.00)
        static let royalBlue = UIColor(red:0.34, green:0.44, blue:0.91, alpha:1.00)
        static let primaryColor = UIColor(red:0.99, green:0.25, blue:0.11, alpha:1.00)
        static let yellow = UIColor(red:0.96, green:0.84, blue:0.29, alpha:1.00)
        static let caranation = UIColor(red:0.94, green:0.38, blue:0.38, alpha:1.00)
        static let lime = UIColor(red:0.72, green:0.82, blue:0.44, alpha:1.00)
        static let fuchsia = UIColor(red:0.78, green:0.49, blue:0.77, alpha:1.00)
        static let tiffanyBlue = UIColor(red:0.02, green:0.71, blue:0.71, alpha:1.00)
        static let checkMark = UIColor(red:0.24, green:0.81, blue:0.56, alpha:1.00)
    }
    
}
extension CGAffineTransform {
    static var highlightedTransform = CGAffineTransform(scaleX: 0.8, y: 0.8)
}

extension CGFloat {
    static func random() -> CGFloat {
        return CGFloat(arc4random()) / CGFloat(UInt32.max)
    }
}

extension UIColor {
    static func random() -> UIColor {
        return UIColor(red:   .random(),
                       green: .random(),
                       blue:  .random(),
                       alpha: 0.10)
    }
}
