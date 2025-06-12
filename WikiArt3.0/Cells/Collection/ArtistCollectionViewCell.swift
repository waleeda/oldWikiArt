//
//  ArtistCollectionViewCell.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-22.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

final class ArtistCollectionViewCell: UICollectionViewCell, ValueCell {
    
    static var defaultReusableId: String = String(describing: ArtistCollectionViewCell.self)
    
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var detailLabel: UILabel!
    @IBOutlet weak var nation: UILabel!
    
    
    override func prepareForReuse() {
        super.prepareForReuse()
        imageView.image = nil
        titleLabel.text = nil
        titleLabel.textColor = .white
        titleLabel.attributedText = nil
        nation.text = nil
        nation.attributedText = nil
        detailLabel.text = nil
        detailLabel.attributedText = nil
    }
    
    override var isHighlighted: Bool {
        didSet {
            if isHighlighted {
                Spring { self.transform = .init(scaleX: 0.9, y: 0.9) }
            }
            else {
                Spring { self.transform = .init(scaleX: 1, y: 1) }
            }
        }
    }
    
    func configureWith(value: Artist, term: String? = nil) {
        if let term = term {
            titleLabel.attributedText = NSMutableAttributedString(title: value.title ?? "", term: term)
        }
        else {
            titleLabel.text = value.title
        }
        
        detailLabel.text = value.totalWorksTitle?.capitalized
        nation.text = String([value.nation, value.year])
        let imageModel = ImageViewModel(value)
        imageView.configureWith(viewModel: imageModel)
    }
}


