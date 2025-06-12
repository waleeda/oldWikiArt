//
//  Created by Waleed Azhar on 2019-08-13.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit


final class ArtistDetailDataSource: DataSource {
    
    private var famousPaintingsDataSource: PaintingRowDataSource?
    
    func appendArtist(_ artist: Artist) -> IndexSet {
        let data: DataType = [[(artist, ArtistTableViewCell.self)]]
        return appendList(with: data)
    }
    
    func appendFamousPaintings(_ realtedPaintings: [Painting], delegate: PaintingRowDataSourceDelegate?) -> IndexSet
    {
        let layout = Current.layoutService.paintingRowFlow()
        self.famousPaintingsDataSource = PaintingRowDataSource(relatedPaintings: realtedPaintings, delegate: delegate)
        
        let collectionViewRow: [(Any, ValueCell.Type)] =
            [(CollectionViewCellModel(dataSouce: famousPaintingsDataSource!, delegate: famousPaintingsDataSource!, layout: layout), CollectionViewTableViewCell.self)]

        let title = [(LineCellViewModel(.init(NSLocalizedString("Famous Works", comment: "").uppercased(), .lightHeading), .none, .clear) as Any, (LineTableViewCell.self as ValueCell.Type))]
       
        return appendList(with: [title + collectionViewRow])
    }
    
    func appendArtistDetails(_ artist2: Artist, _ details: ArtistDetails) -> IndexSet
    {
        var artist: Artist
        if artist2.image != nil {
            artist = artist2
        }
        else {
            artist = Artist(details: details)
        }
                
        let rows:[(Any, ValueCell.Type)] = ArtistDetailsDisplay(details: details).displayDict.map {
            (ColumnCellViewModel(NSLocalizedString($0.0, comment: "").uppercased(), $0.1, .row), (ColumnTableViewCell.self))
        }
        let data: DataType = [ [(artist, ArtistTableViewCell.self)] + rows ]
        return appendList(with: data)
    }
    
    func appendSeeAll(_ action: @escaping Action) -> IndexSet {

        let data: DataType = [
            [(ButtonCellViewModel(config: { b in  b.setTitle(NSLocalizedString("See All Paintings", comment: "").uppercased(), for: .normal) },
                                  pressed: action), ButtonTableViewCell.self)]
                ]
        return appendList(with: data)
    }
    
    override func configureCell(tableCell cell: UITableViewCell, withValue value: Any) {
        switch (cell, value) {
        case let (cell as LineTableViewCell, value as LineCellViewModel):
            cell.configureWith(value: value)
        case let (cell as ColumnTableViewCell, value as ColumnCellViewModel):
            cell.configureWith(value: value)
        case let (cell as ArtistTableViewCell, value as Artist):
            cell.configureWith(value: value)
        case let (cell as ImageTableViewCell, value as ImageViewModel):
            cell.configureWith(value: value)
        case let (cell as TitleTableViewCell, value as TitleCellViewModel):
            cell.configureWith(value: value)
        case let (cell as CollectionViewTableViewCell, value as CollectionViewCellModel):
            cell.configureWith(value: value)
        case let (cell as ButtonTableViewCell, value as ButtonCellViewModel):
            cell.configureWith(value: value)
        default:
            assertionFailure("Unrecognized (cell, viewModel) combo.")
        }
    }
}
