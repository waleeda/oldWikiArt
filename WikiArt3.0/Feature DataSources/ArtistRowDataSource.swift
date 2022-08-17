//
//  RelatedPaintingsDataSource.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-21.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

protocol ArtistRowDataSourceDelegate: class {
    func newHeightForArtistRow()
    func artistRow(_ row :ArtistRowDataSource,_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath)
}

final class ArtistRowDataSource: DataSource, UICollectionViewDelegate {

    private var term: String?
    private(set) var cellHeight: CGFloat = 0 {
        didSet {
            delegate?.newHeightForArtistRow()
        }
    }
    
    weak var delegate: ArtistRowDataSourceDelegate?
    
    init(artists: [Artist], delegate: ArtistRowDataSourceDelegate?, term: String?) {
        self.term = term
        self.delegate = delegate
        let data:[(Any, ValueCell.Type)] = artists.map { ($0 as Any, ArtistCollectionViewCell.self as ValueCell.Type) }
        super.init()
        cellHeight = artists.isEmpty ? 0 : 240*1.3
        _ = appendList(with: [data])
        
    }
    
    override func registerClasses(collectionView: UICollectionView?) {
        collectionView?.register(cell: ArtistCollectionViewCell.self)
    }
    
    override func configureCell(collectionView cell: UICollectionViewCell, withValue value: Any) {
        switch (cell, value) {
        case let (cell as ArtistCollectionViewCell, value as Artist):
            cell.configureWith(value: value, term: term)
        default:
            assertionFailure("Unrecognized (cell, viewModel) combo.")
        }
    }
   
}

extension ArtistRowDataSource: UICollectionViewDelegateFlowLayout {
        
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let width: CGFloat = 240.0
        let cellSize = CGSize(width: width, height: width*1.3)
        return cellSize
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        delegate?.artistRow(self, collectionView, didSelectItemAt: indexPath)
    }
    
}
