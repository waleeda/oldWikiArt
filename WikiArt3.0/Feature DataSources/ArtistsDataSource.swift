//
//  RelatedPaintingsDataSource.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-21.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

final class ArtistsDataSource: DataSource {

    func append(artists: [Artist]) -> [IndexPath] {
        let data:[(Any, ValueCell.Type)] = artists.map { ($0, ArtistCollectionViewCell.self) }
        return appendFirstSection(with: [data])
    }
    
    override func registerClasses(collectionView: UICollectionView?) {
        collectionView?.register(cell: ArtistCollectionViewCell.self)
    }
    
    override func configureCell(collectionView cell: UICollectionViewCell, withValue value: Any) {
        switch (cell, value) {
        case let (cell as ArtistCollectionViewCell, value as Artist):
            cell.configureWith(value: value)
        default:
            assertionFailure("Unrecognized (cell, viewModel) combo.")
        }
    }
}
