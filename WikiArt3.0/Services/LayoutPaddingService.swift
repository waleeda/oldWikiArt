//
//  LayoutPaddingService.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-24.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

protocol LayoutPaddingServiceProtocol {
    func bottomPaddingForItem(with itemSize: CGSize, with painting: Painting) -> CGFloat
}

final class LayoutPaddingService: LayoutPaddingServiceProtocol {
    
    private var cache: [String: [CGFloat: CGFloat]] = [:]
    
    static fileprivate let cell: PaintingCollectionViewCell = {
        let result = Bundle.main.loadNibNamed("PaintingCollectionViewCell", owner: nil, options: nil)?.first as? PaintingCollectionViewCell
        return result ?? PaintingCollectionViewCell()
    }()
    
    let artistLabel: UILabel = {
        let temp = UILabel()
        temp.font = LayoutPaddingService.cell.artistName.font
        temp.numberOfLines = 99
        return temp
    }()
    
    let titleLabel: UILabel = {
        let temp = UILabel()
        temp.font = LayoutPaddingService.cell.titleView.font
        temp.numberOfLines = 99
        return temp
    }()
    
    let dateLabel: UILabel = {
        let temp = UILabel()
        temp.font = LayoutPaddingService.cell.date.font
        temp.numberOfLines = 99
        return temp
    }()
    
   
    
    func bottomPaddingForItem(with itemSize: CGSize, with painting: Painting) -> CGFloat {
        if let cachePadding = cache[painting.id]?[itemSize.width] {
            return cachePadding
        }

        titleLabel.text = painting.title
        artistLabel.text = painting.artistName
        dateLabel.text = painting.year
        
        let fittingSize = CGSize(width: itemSize.width - LayoutPaddingService.cell.layoutMargins.left - LayoutPaddingService.cell.layoutMargins.right,
                                 height: CGFloat.greatestFiniteMagnitude)
        
        let artistNameSize = titleLabel.sizeThatFits(fittingSize)
        let titleSize = artistLabel.sizeThatFits(fittingSize)
        let dateSize = dateLabel.sizeThatFits(fittingSize)
        
        let padding = artistNameSize.height + titleSize.height + dateSize.height + 18 + 2
       
        if cache[painting.id] == nil {
            cache.updateValue( [ itemSize.width : padding] , forKey: painting.id)
        }
        else {
            cache[painting.id]?.updateValue(padding, forKey: itemSize.width)
        }
        
        return padding
    }
}

