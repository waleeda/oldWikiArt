//
//  ViewControllerModel.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-01.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit

class DataSource: NSObject {
    typealias DataType = [[(model: Any, cellType: ValueCell.Type)]]
    private(set) var data: DataType = []
    
    final var sections: Int {
        return data.count
    }
    
    final func rows(for section: Int) -> Int {
        guard (0..<data.count).contains(section) else { return 0 }
        return data[section].count
    }
    
    func titleForHeader(in section: Int) -> String? { nil }
    
    open func configureCell(tableCell cell: UITableViewCell, withValue value: Any) {}
    open func configureCell(collectionView cell: UICollectionViewCell, withValue value: Any) {}
    
    open func registerClasses(collectionView: UICollectionView?) {}

    open func registerClasses(tableView: UITableView?) {}
    
    // MARK: MODIFY DATA CONTENT
    
    final func replaceRow(at indexPath: IndexPath, with row: (Any, ValueCell.Type)) -> IndexPath {
        data[indexPath.section][indexPath.row] = row
        return indexPath
    }
    
    @discardableResult
    final func append(section: Int, with content: DataType) -> [IndexPath] {
        let rowData = content.flatMap { $0 }
        let startCount = data[section].count
        let endCount = startCount + rowData.count
        data[section] = data[section] + rowData
       
        return (startCount..<endCount).map { .init(row: $0, section: section) }
    }
    
    final func prepend(section: Int, with content: DataType) -> [IndexPath] {
        let rowData = content.flatMap { $0 }
        let startCount = data[section].count
        data[section] = rowData + data[section]
        return (0..<startCount).map { .init(row: $0, section: section) }
    }
    
    final func appendFirstSection(with content: DataType) -> [IndexPath] {
        guard data.count > 0 else {
            data = content
            return (0..<data.count).map { .init(row: $0, section:0) }
        }
        return self.append(section: 0, with: content)
    }
    
    final func prependFirstSection(with content: DataType) -> [IndexPath] {
        guard data.count > 0 else {
            data = content
            return (0..<data.count).map { .init(row: $0, section:0) }
        }
        return prepend(section: 0, with : content)
    }
    
    final func appendList(with content: DataType) -> IndexSet {
        let startCount = data.count
        let endCount = startCount + content.count
        data = data + content
        return .init(startCount..<endCount)
    }
    
    final func prependList(with content: DataType) -> IndexSet {
        let startCount = data.count
        let endCount = content.count
        data = content + data
        return .init(startCount..<endCount)
    }
    
    final func insertSection(at index: Int, content:DataType) -> IndexSet {
        let newSection = content.flatMap { $0 }
        data = Array(data[0..<index]) + [newSection] + Array(data[index..<data.count])
        return .init(arrayLiteral: index)
    }
    
    final func insertSections(from index: Int, content:DataType) -> IndexSet {
        data = Array(data[0..<index]) + content + Array(data[index..<data.count])
        return .init(arrayLiteral: index)
    }
    
    final func removeRow(at indexPath: IndexPath) -> IndexPath {
        data[indexPath.section].remove(at: indexPath.row)
        return indexPath
    }
    
    final func removeRows(at indexPaths: [IndexPath]) -> [IndexPath] {
        return indexPaths.map { self.removeRow(at: $0) }
    }
    
    final func removeSection(at index: Int) -> IndexSet {
        data = Array(data[0..<index]) + Array(data[(index + 1)..<data.count])
        return .init(arrayLiteral: index)
    }
    
    final func removeLastSection() -> IndexSet {
        guard data.count > 0 else { return .init() }
        return removeSection(at: data.count - 1)
    }
    
    @discardableResult
    final func removeAll() -> IndexSet {
        let startCount = 0
        let endCount = data.count
        data = []
        return .init(startCount..<endCount)
    }
    
    final subscript(indexPath: IndexPath) -> Any {
        return self.data[indexPath.section][indexPath.item].model
    }
    
    public final subscript(itemSection itemSection: (item: Int, section: Int)) -> Any {
        return self.data[itemSection.section][itemSection.item].model
    }
    
    public final subscript(section section: Int) -> [Any] {
        return self.data[section].map { $0.model }
    }
}

extension DataSource: UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return sections
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return rows(for: section)
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let (value, CellType) = data[indexPath.section][indexPath.row]
        var cell = tableView.dequeueReusableCell(withIdentifier: String(describing: CellType))
        if cell == nil {
            tableView.register(cell: CellType)
            cell = tableView.dequeueReusableCell(withIdentifier: CellType.defaultReusableId)!
        }
        self.configureCell(tableCell: cell!, withValue: value)
        return cell!
    }
}

extension DataSource: UICollectionViewDataSource {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return sections
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return rows(for: section)
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let (value, CellType) = data[indexPath.section][indexPath.row]
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: String(describing: CellType), for: indexPath)
        self.configureCell(collectionView: cell, withValue: value)
        return cell
    }
}
