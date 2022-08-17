//
//  CGSizeExtensions.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-19.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

extension CGSize{
    var aspectRatioHeight: CGFloat{
        return (self.height / self.width)
    }
    
    var aspectRatioWidth: CGFloat{
        return (self.width  / self.height)
    }
}

extension UISegmentedControl {
    
    static func paintingsLayoutsControl() -> UISegmentedControl {
        var segment: UISegmentedControl
        if #available(iOS 13, *) {
            segment = UISegmentedControl(items: Icon.systemNameLayouts)
        } else {
            segment = UISegmentedControl(items: Icon.layouts as [Any])
        }
        
        segment.apportionsSegmentWidthsByContent = false
        segment.selectedSegmentIndex = 1
        
        return segment
    }
    
    static func artistsSortControl() -> UISegmentedControl {
        let segment: UISegmentedControl = UISegmentedControl(items: ArtistPaintingListSort.all.map { $0 .title} )
        segment.selectedSegmentIndex = 1
    
        return segment
    }
    
}

