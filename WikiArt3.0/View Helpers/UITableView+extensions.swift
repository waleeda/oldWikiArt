//
//  WATableViewController.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-01.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit
typealias Action = (UIControl) -> ()

extension UITableView {

    func register(cell: ValueCell.Type) {
        let reuseID = cell.defaultReusableId
        if Bundle.main.path(forResource: reuseID, ofType: "nib") != nil {
            register(UINib(nibName: reuseID, bundle: nil), forCellReuseIdentifier: reuseID)
        }
        else {
            register(cell, forCellReuseIdentifier: reuseID)
        }
    }
    
    func register<A: ValueCell>(cells: [A.Type]) {
        cells.forEach { register(cell: $0) }
    }
    
    func dequeue<A: UITableViewCell>(indexPath: IndexPath) -> A {
        return dequeueReusableCell(withIdentifier: String(describing: A.self), for: indexPath) as! A
    }
    
    func scrollToTop(animated: Bool) {
        scrollRectToVisible(.init(x: 1, y: 1, width: 1, height: 1), animated: animated)
    }
}

