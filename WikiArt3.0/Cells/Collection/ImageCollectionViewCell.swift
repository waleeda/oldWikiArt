//
//  ImageCollectionViewCell.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-19.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

class ImageCollectionViewCell: UICollectionViewCell, ValueCell {
    static var defaultReusableId: String = String(describing: ImageCollectionViewCell.self)
    
    @IBOutlet weak var imageView: UIImageView!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        imageView.image = nil
    }
    
    override var isHighlighted: Bool {
        didSet {
            if isHighlighted {
                       Spring {
                           self.transform = .init(scaleX: 0.97, y: 0.97)
                       }
                   }
            else if !isHighlighted {
                       Spring {
                           self.transform = .init(scaleX: 1, y: 1)
                       }
                   }
        }
    }

    
    func configureWith(value: ImageViewModel) {
        imageView.configureWith(viewModel: value)
    }
    
}
