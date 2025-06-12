//
//  RowLayout.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-31.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

class RowLayout: UICollectionViewFlowLayout {

    override func layoutAttributesForElements(in rect: CGRect) -> [UICollectionViewLayoutAttributes]? {
        updateAtts(super.layoutAttributesForElements(in: rect))
    }
    
    override func layoutAttributesForItem(at indexPath: IndexPath) -> UICollectionViewLayoutAttributes? {
        guard let att = super.layoutAttributesForItem(at: indexPath) else { return nil }
        return updateAtts([att])?.first
    }
    
    func updateAtts(_ att: [UICollectionViewLayoutAttributes]?) -> [UICollectionViewLayoutAttributes]? {
        if let attributes = att {
            attributes.forEach {
                $0.frame = .init(origin: .init(x: $0.frame.origin.x, y: 8.0),
                                 size: $0.frame.size)
            }
        }
        return att
    }
}
