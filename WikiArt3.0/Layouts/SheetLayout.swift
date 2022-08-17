//
//  WACollectionFixedHeightLayout.swift
//  WikiArt
//
//  Created by waleed azhar on 2017-05-01.
//  Copyright © 2017 waleed azhar. All rights reserved.
//

import UIKit

final class SheetLayout: UICollectionViewLayout {
    private let minRowHeight:CGFloat
    private var attributes:[UICollectionViewLayoutAttributes] = []
    private var cellSpacing:CGFloat
    private var minItemWidth:CGFloat
    private var maximumItemsPerRow: Int
    unowned var dataSource: ItemSizeLayoutDataSource
    
    init(minRowHeight: CGFloat = 300.0,
         minItemWidth: CGFloat = 150.0,
         maximumItemsPerRow: Int = 3,
         cellSpacing: CGFloat,
         dataSource: ItemSizeLayoutDataSource) {
        self.minRowHeight = minRowHeight
        self.minItemWidth = minItemWidth
        self.maximumItemsPerRow = maximumItemsPerRow
        self.cellSpacing = cellSpacing
        self.dataSource = dataSource
        super.init()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override var collectionViewContentSize: CGSize {
        if attributes.isEmpty { return .zero }
        return CGSize(width: collectionView!.bounds.width, height: attributes.last!.frame.maxY + cellSpacing)
    }
    
    override func prepare() {
        attributes = []
        append(from: 0)
    }
    
    func append(from row: Int) {
        let viewWidth = self.collectionView!.frame.inset(by: self.collectionView!.safeAreaInsets).width
        
        let section = 0
        let numberOfItems = dataSource.numberOfItemsInLayout()
        var itemSizes:[CGSize] = []
        
        for itemRow in row..<numberOfItems {
            let size = dataSource.layout(dimensionsForItemAt: .init(row: itemRow, section: section))
            itemSizes.append(size)
        }
        
        let standardItemHeight = minRowHeight as CGFloat
        
        var standardizedItemSizes = itemSizes.map {
            CGSize(width: ($0.aspectRatioWidth * standardItemHeight), height: standardItemHeight)
        }
        
        // Build rows
        
        func rowWidthForRowItemCount(_ itemCount: UInt) -> CGFloat {
            viewWidth - (cellSpacing * CGFloat(itemCount + 1))
        }
        
        func rowWidthForItemSizes(_ itemSizes: [CGSize]) -> CGFloat {
            itemSizes.reduce(0.0) { $0 + $1.width }
        }
        
        func itemWidthCheck(_ itemSizes: [CGSize]) -> Bool {
            var result = true
            itemSizes.forEach {
                if !($0.width > minItemWidth) {
                    result = false
                }
            }
            return result
        }
    
        var itemSizeRows: [[CGSize]] = []
        var currRow: [CGSize] = []
        
        while let nextItemSize = standardizedItemSizes.first {
            let nextRow = currRow + [nextItemSize]
            if
                ((rowWidthForItemSizes(nextRow) > rowWidthForRowItemCount(UInt(nextRow.count))) || currRow.count == maximumItemsPerRow),
                !currRow.isEmpty
            {
                itemSizeRows.append(currRow)
                currRow = []
            }
            else {
                currRow = nextRow
                standardizedItemSizes.removeFirst()
            }
        }
        
        if !currRow.isEmpty {
            itemSizeRows.append(currRow)
        }
        
        // build attributes
        
        let x0: CGFloat = self.collectionView!.safeAreaInsets.left + cellSpacing
        var y0: CGFloat = cellSpacing
        
        var itemRow = row
        itemSizeRows.forEach
        {
            let rowWidth = rowWidthForRowItemCount(UInt($0.count))
            let attributeHeight = CGSize(width: rowWidthForItemSizes($0), height: standardItemHeight).aspectRatioHeight * rowWidth
            
            var rowX = x0
            let rowY = y0
            
            $0.forEach {
                let indexPath = IndexPath(item: itemRow, section: section)
                let frame = CGRect(x: rowX, y: rowY, width: attributeHeight * $0.aspectRatioWidth, height: attributeHeight)
                let attribute = UICollectionViewLayoutAttributes(forCellWith: indexPath)
                attribute.frame = frame
                itemRow += 1
                rowX = frame.maxX + cellSpacing
                attributes.append(attribute)
            }
            
            y0 += attributeHeight + cellSpacing
        }
    }
    
    override func layoutAttributesForElements(in rect: CGRect) -> [UICollectionViewLayoutAttributes]? {
        if rect.maxY > collectionViewContentSize.height {
            append(from: attributes.count)
        }
        return attributes
    }
    
    override func layoutAttributesForItem(at indexPath: IndexPath) -> UICollectionViewLayoutAttributes? {
        if indexPath.row >= attributes.count {
            append(from: attributes.count)
        }
        return attributes[indexPath.row]
    }
    
    private var oldBounds = CGRect.zero
    override func shouldInvalidateLayout(forBoundsChange newBounds: CGRect) -> Bool {
        defer {
            oldBounds = newBounds
        }
        
        if oldBounds.width != newBounds.width {
            return true
        }
     
        return false
    }

}
