//
//  WATableViewController.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-01.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

extension UICollectionView {

    func register<A: UICollectionViewCell>(cell:A.Type) {
        let reuseID = String(describing: cell)
        if Bundle.main.path(forResource: reuseID, ofType: "nib") != nil {
            register(UINib(nibName: reuseID, bundle: nil), forCellWithReuseIdentifier: reuseID)
        }
        else {
            register(cell, forCellWithReuseIdentifier: reuseID)
        }
    }
    
    func register<A: UICollectionViewCell>(cells: [A.Type]) {
        cells.forEach { register(cell: $0) }
    }
    
    func dequeue<A: UICollectionViewCell>(indexPath: IndexPath) -> A {
        return dequeueReusableCell(withReuseIdentifier: String(describing: A.self), for: indexPath) as! A
    }
    
    func scrollToTop() {
        scrollRectToVisible(.init(x: 1, y: 1, width: 1, height: 1), animated: true)
    }
}

