//
//  WACollectionFixedHeightLayout.swift
//  WikiArt
//
//  Created by waleed azhar on 2017-05-01.
//  Copyright © 2017 waleed azhar. All rights reserved.
//

import UIKit

protocol ItemSizeLayoutDataSource: class {
    func numberOfItemsInLayout() -> Int
    func layout(dimensionsForItemAt indexPath: IndexPath) -> CGSize
    func bottomPaddingForItem(at indexPath: IndexPath, with itemSize: CGSize) -> CGFloat?
}

class ColumnStackLayout: UICollectionViewLayout {
    private let numberOfColumns: UInt
    private var attributes:[UICollectionViewLayoutAttributes] = []
    private var cellSpacing:CGFloat
    unowned var dataSource: ItemSizeLayoutDataSource
    private var maxY: CGFloat = 0
    
    init(numberOfColumns: UInt,
         cellSpacing: CGFloat,
         dataSource: ItemSizeLayoutDataSource) {
        self.numberOfColumns = numberOfColumns
        self.cellSpacing = cellSpacing
        self.dataSource = dataSource
        super.init()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override var collectionViewContentSize: CGSize {
        if attributes.isEmpty { return .zero }
        return CGSize(width: self.collectionView!.bounds.width, height: maxY + cellSpacing)
    }
    
    override func prepare() {
        attributes = []
        
        let viewWidth = self.collectionView!.frame.inset(by: self.collectionView!.safeAreaInsets).width
        
        let section = 0
        let numberOfItems = dataSource.numberOfItemsInLayout()
        
        var itemSizes:[CGSize] = []
        for itemRow in 0..<numberOfItems {
            let size = dataSource.layout(dimensionsForItemAt: .init(row: itemRow, section: section))
            itemSizes.append(size)
        }
        
        func rowWidthForRowItemCount(_ itemCount: UInt) -> CGFloat {
            viewWidth - (cellSpacing * CGFloat(itemCount + 1))
        }
        
        // Standardize Sizes
        let standardItemWidth = rowWidthForRowItemCount(numberOfColumns) / CGFloat(numberOfColumns)
        
        var standardizedItemSizes = itemSizes.map {
            CGSize(width: standardItemWidth , height: ($0.aspectRatioHeight * standardItemWidth))
        }
        
        // Build Rows
        var itemSizeRows: [[CGSize]] = []
        var currRow: [CGSize] = []
        
        while let nextItemSize = standardizedItemSizes.first {
            let nextRow = currRow + [nextItemSize]
            if currRow.count == numberOfColumns
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
        
        let originY: CGFloat = cellSpacing
        var yCords: [CGFloat] = (0..<numberOfColumns).map { _ in originY }
        let xCords: [CGFloat] = (0..<numberOfColumns).map {
            let widths = (standardItemWidth * CGFloat($0))
            let spaces = (cellSpacing * CGFloat($0 + 1))
            return self.collectionView!.safeAreaInsets.left + widths + spaces
        }
        
        var itemRow = 0
        itemSizeRows.forEach
        {
            $0.forEach {
                let yCordIndex = yCords.firstIndex(of: yCords.min() ?? yCords[itemRow % Int(numberOfColumns)]) ?? (itemRow % Int(numberOfColumns))
                let indexPath = IndexPath(item: itemRow, section: section)
                let padding = dataSource.bottomPaddingForItem(at: indexPath, with: CGSize(width: $0.width, height: $0.height)) ?? 0.0
                let frame = CGRect(x: xCords[yCordIndex], y: yCords[yCordIndex], width: $0.width, height: $0.height + padding)
                let attribute = UICollectionViewLayoutAttributes(forCellWith: indexPath)
                attribute.frame = frame
                yCords[yCordIndex] = yCords[yCordIndex] + frame.height + cellSpacing
                itemRow += 1
                attributes.append(attribute)
            }
        }
        
        maxY = yCords.max() ?? 0.0
       // attributes = attributeRows.flatMap { $0 }
    }
    
    override func layoutAttributesForElements(in rect: CGRect) -> [UICollectionViewLayoutAttributes]? {
        return attributes
    }
    
    override func layoutAttributesForItem(at indexPath: IndexPath) -> UICollectionViewLayoutAttributes? {
        if indexPath.row >= attributes.count {
            prepare()
        }
        return attributes[indexPath.row]
    }
    
    override func layoutAttributesForDecorationView(ofKind elementKind: String,
                                                    at indexPath: IndexPath) -> UICollectionViewLayoutAttributes? {
        return nil
    }
    
    override func layoutAttributesForSupplementaryView(ofKind elementKind: String,
                                                       at indexPath: IndexPath) -> UICollectionViewLayoutAttributes? {
        return nil
    }
    
    override func shouldInvalidateLayout(forBoundsChange newBounds: CGRect) -> Bool {
        
        if collectionViewContentSize.width != newBounds.width {
            return true
        }
        return false
    }

}
