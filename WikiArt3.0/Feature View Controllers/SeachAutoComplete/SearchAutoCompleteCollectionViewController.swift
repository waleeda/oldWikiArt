//
//  PaintingCollectionViewController.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-19.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

class SearchAutoCompleteCollectionViewController: CollectionViewController, ResultViewController, AlertViewController {
    
    internal var autoComplete: SearchAutoComplete
    
    init(autoComplete: SearchAutoComplete) {
        self.autoComplete = autoComplete
        super.init(collectionViewLayout: UICollectionViewFlowLayout())
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        collectionView.backgroundColor = .groupTableViewBackground
        navigationItem.title = autoComplete.value
        navigationItem.prompt = autoComplete.description
    }
    
}
