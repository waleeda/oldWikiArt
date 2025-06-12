//
//  LayoutService.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-24.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

protocol LayoutService {
    func collectionViewLayout(for layout: Layout, dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout
    
    func paintingListColumnStack(dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout
    func paintingColumnStack(dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout
    func paintingSheet(dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout
    func artistColumnStack(dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout
    func paintingRowFlow() -> UICollectionViewLayout
}

extension LayoutService {
    func collectionViewLayout(for layout: Layout, dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout {
        switch layout {
        case .sheet:
            return paintingSheet(dataSource: dataSource)
        case .list:
            return paintingListColumnStack(dataSource: dataSource)
        case .column:
            return paintingColumnStack(dataSource: dataSource)
        case .artist:
            return artistColumnStack(dataSource: dataSource)
        case .paintingRow:
            return paintingRowFlow()
        }
    }
}

struct PadLayoutService: LayoutService {
    
    
    func paintingListColumnStack(dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout {
        ColumnStackLayout(numberOfColumns: 1, cellSpacing: 4, dataSource: dataSource)
    }
    
    func paintingColumnStack(dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout {
        ColumnStackLayout(numberOfColumns: 3, cellSpacing: 4, dataSource: dataSource)
    }
    
    func paintingSheet(dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout {
        SheetLayout(minRowHeight: 240.0, maximumItemsPerRow: 4, cellSpacing: 1, dataSource: dataSource)
    }
    
    func artistColumnStack(dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout {
        ColumnStackLayout(numberOfColumns: 4, cellSpacing: 4, dataSource: dataSource)
    }
    
    func paintingRowFlow() -> UICollectionViewLayout {
        let layout = RowLayout()
        layout.scrollDirection = .horizontal
        layout.minimumInteritemSpacing = 4
        layout.sectionInset = .init(top: 0, left: 20, bottom: 0, right: 20)
        return layout
    }
}

struct PhoneLayoutService: LayoutService {
    func paintingListColumnStack(dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout {
        ColumnStackLayout(numberOfColumns: 1, cellSpacing: 0, dataSource: dataSource)
    }
    
    func paintingColumnStack(dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout {
        ColumnStackLayout(numberOfColumns: 2, cellSpacing:4, dataSource: dataSource)
    }
    
    func paintingSheet(dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout {
        SheetLayout(minRowHeight: 160.0, cellSpacing: 2, dataSource: dataSource)
    }
    
    func artistColumnStack(dataSource: ItemSizeLayoutDataSource) -> UICollectionViewLayout {
        ColumnStackLayout(numberOfColumns: 2, cellSpacing: 8, dataSource: dataSource)
    }
    
    func paintingRowFlow() -> UICollectionViewLayout {
        let layout = RowLayout()
        layout.scrollDirection = .horizontal
        layout.sectionInset = .init(top: 0, left: 20, bottom: 0, right: 20)
        return layout
    }
}
