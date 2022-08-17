//
//  CollectionViewTableViewCell.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-21.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

struct CollectionViewCellModel {
    let dataSouce: DataSource
    let delegate: UICollectionViewDelegate
    let layout: UICollectionViewLayout
}

final class CollectionViewTableViewCell: UITableViewCell, ValueCell {
    static var defaultReusableId: String = String(describing: CollectionViewTableViewCell.self)
    
    @IBOutlet public weak var collectionView: UICollectionView!
    
    override func layoutSubviews() {
        if intrinsicContentSize != collectionView.contentSize {
            self.invalidateIntrinsicContentSize()
        }
    }
    
    override var intrinsicContentSize: CGSize {
        return collectionView.contentSize
    }
    
    func configureWith(value vm: CollectionViewCellModel) {
        collectionView.delegate = vm.delegate
        vm.dataSouce.registerClasses(collectionView: collectionView)
        collectionView.dataSource = vm.dataSouce
        collectionView.setCollectionViewLayout(vm.layout, animated: true)
    }
}
