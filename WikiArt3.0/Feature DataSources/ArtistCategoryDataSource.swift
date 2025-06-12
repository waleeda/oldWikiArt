//
//  SelectCategoryDataSource.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-12.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit

final class ArtistCategoryDataSource: DataSource {
    
    func setCategories() -> IndexSet {
        removeAll()
        let apendSet = prependList(with: [ArtistCategory.all.map { ($0, OptionTableViewCell.self) }])
        return apendSet
    }
    
    func set(category: ArtistCategory, sections: [ArtistsSection]) -> IndexSet {
        removeAll()
        return appendList(with: [ sections.map { ($0, OptionTableViewCell.self) }])
    }
 
    override func configureCell(tableCell cell: UITableViewCell, withValue value: Any) {
        print(cell.self.description)
        switch (cell, value) {
        case let (cell as OptionTableViewCell, value as ArtistCategory):
            cell.configureWith(value: value)
        case let (cell as OptionTableViewCell, value as ArtistsSection):
            cell.configureWith(value: value)
        default:
            assertionFailure("Unrecognized (cell, viewModel) combo.")
        }
    }
    
}
