//
//  RelatedPaintingsDataSource.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-21.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

protocol PaintingRowDataSourceDelegate: class {
    func paintingRow(_ row :PaintingRowDataSource,_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath)
    func newHeight()
}

final class PaintingRowDataSource: DataSource, UICollectionViewDelegate {

    private var term: String?
    
    private(set) var cellHeight: CGFloat = 320 {
        didSet {
            delegate?.newHeight()
        }
    }
    
    weak var delegate: PaintingRowDataSourceDelegate?
    
    init(relatedPaintings: [Painting], delegate: PaintingRowDataSourceDelegate?, term: String? = nil) {
        self.term = term
        self.delegate = delegate
        let data:[(Any, ValueCell.Type)] = relatedPaintings.map { ($0 as Any, PaintingCollectionViewCell.self as ValueCell.Type) }
        super.init()
        _ = appendList(with: [data])
    }

    override func registerClasses(collectionView: UICollectionView?) {
        collectionView?.register(cell: PaintingCollectionViewCell.self)
    }
    
    override func configureCell(collectionView cell: UICollectionViewCell, withValue value: Any) {
        switch (cell, value) {
        case let (cell as PaintingCollectionViewCell, value as Painting):
            cell.configureWith(value: value, term: term)
        default:
            assertionFailure("Unrecognized (cell, viewModel) combo.")
        }
    }
}

extension PaintingRowDataSource: UICollectionViewDelegateFlowLayout {
        
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let paintingHeight = 240
        let painting = data[indexPath.section][indexPath.row].model as! Painting
        let paintingSize = CGSize(width: paintingHeight*painting.width/painting.height, height: paintingHeight)
        let padding = Current.paddingService.bottomPaddingForItem(with: paintingSize, with: painting)
        let height = CGFloat(paintingHeight) + padding
        if height > cellHeight {
            cellHeight = height
        }
        let cellSize = CGSize(width: paintingSize.width, height: height)
        return cellSize
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        delegate?.paintingRow(self, collectionView, didSelectItemAt: indexPath)
    }
    
}
