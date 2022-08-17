//
//  PaintingCollectionViewCell.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-21.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

final class PaintingCollectionViewCell: UICollectionViewCell, ValueCell {
   static var defaultReusableId: String = String(describing: PaintingCollectionViewCell.self)
    

    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var titleView: UILabel!
    @IBOutlet weak var artistName: UILabel!
    @IBOutlet weak var date: UILabel!
    @IBOutlet weak var stack: UIStackView!
    
    override var isHighlighted: Bool {
        didSet {
            if isHighlighted {
                Spring { self.transform = .init(scaleX: 0.9, y: 0.9); self.alpha = 0.8 }
            }
            else {
                Spring { self.transform = .init(scaleX: 1, y: 1); self.alpha = 1 }
            }
        }
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        
        titleView.text = nil
        titleView.attributedText = nil
        artistName.text = nil
        date.text = nil
        imageView.image = nil
    }
    
    func configureWith(value: Painting, term: String? = nil) {
        if let term = term {
            titleView.attributedText = NSMutableAttributedString(title: value.title, term: term)
        }
        else {
            titleView.text = value.title
        }
        artistName.text = value.artistName
        date.text = value.year
        let imageModel = ImageViewModel(value)
        imageView.configureWith(viewModel: imageModel)
    }
}
