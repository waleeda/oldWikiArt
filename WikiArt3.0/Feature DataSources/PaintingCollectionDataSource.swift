//
//  PaintingListDataSource.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-12.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit

internal class PaintingCollectionDataSource: DataSource {
    
    private var cellType : ValueCell.Type = ImageCollectionViewCell.self
    
    override func registerClasses(collectionView: UICollectionView?) {
        collectionView!.register(cell: PaintingCollectionViewCell.self)
        collectionView!.register(cell: ImageCollectionViewCell.self)
    }
    
    func appendPaintings(_ paintings: [Painting]) -> [IndexPath] {
           return appendFirstSection(with: [ paintings.flatMap { [ ($0, cellType) ] }])
    }
    
    func change(to layout: Layout) {
        switch layout {
        case .sheet:
            cellType = ImageCollectionViewCell.self
        case .column, .list:
            cellType = PaintingCollectionViewCell.self
        default:
            return
        }
        let paintings = data[0].map { $0.model as! Painting }
        removeAll()
        _ = appendPaintings(paintings)
    }
    
    override func configureCell(collectionView cell: UICollectionViewCell, withValue value: Any) {
        switch (cell, value) {
        case let (cell as ImageCollectionViewCell, value as Painting):
            cell.configureWith(value: .init(value))
        case let (cell as PaintingCollectionViewCell, value as Painting):
            cell.configureWith(value: value)
        default:
            assertionFailure("Unrecognized (cell, viewModel) combo.")
        }
    }
}
