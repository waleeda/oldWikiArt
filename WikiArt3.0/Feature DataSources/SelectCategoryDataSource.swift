//
//  SelectCategoryDataSource.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-12.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit

final class SelectCategoryDataSource: DataSource {
    
    private var loadingCellIndexPaths: [IndexPath]?
    
    final func appendActivityIndicator() -> [IndexPath] {
        guard loadingCellIndexPaths == nil else { return .init() }
        loadingCellIndexPaths = appendFirstSection(with: [[(LoadingCellViewModel(), LoadingIndicatorTableViewCell.self)]])
        return loadingCellIndexPaths!
    }
    
    final func removeActivityIndicator() -> [IndexPath] {
        guard let indexPaths = loadingCellIndexPaths else { return .init() }
        loadingCellIndexPaths = nil
        return removeRows(at: indexPaths)
    }
    
    func setCategories() -> IndexSet {
        removeAll()
        let apendSet = prependList(with: [PaintingCategory.all.map { ($0, OptionTableViewCell.self) }])
        return apendSet
    }
    
    func set(category: PaintingCategory, sections: [PaintingSection]) -> IndexSet {
        removeAll()
        return appendList(with: [ sections.map { ($0, OptionTableViewCell.self) }])
    }
 
    override func configureCell(tableCell cell: UITableViewCell, withValue value: Any) {
        print(cell.self.description)
        switch (cell, value) {
        case let (cell as OptionTableViewCell, value as PaintingCategory):
            cell.configureWith(value: value)
        case let (cell as OptionTableViewCell, value as PaintingSection):
            cell.configureWith(value: value)
        default:
            assertionFailure("Unrecognized (cell, viewModel) combo.")
        }
    }
    
}
