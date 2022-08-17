//
//  UIPaintingSearchResultTableViewCell.swift
//  WikiArt2.0
//
//  Created by Waleed Azhar on 2017-10-09.
//  Copyright © 2017 Waleed Azhar. All rights reserved.
//

import UIKit

final class ImageTableViewCell: UITableViewCell, ValueCell {
    
    static var defaultReusableId: String = ImageTableViewCell.description()
    
    private(set) var paintingImageView = UIImageView()

    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        paintingImageView.backgroundColor = .lightGray
        paintingImageView.contentMode = .scaleAspectFill
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        contentView.addSubview(paintingImageView)
        clipsToBounds = true
        
        !paintingImageView
        contentView -| paintingImageView
        contentView |- paintingImageView
        contentView ^ paintingImageView
        contentView !^ paintingImageView
        backgroundColor = .clear
        
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        
    }
    
    override func setHighlighted(_ highlighted: Bool, animated: Bool) {
        if highlighted {
            Spring {
                self.transform = .init(scaleX: 0.97, y: 0.97)
            }
        }
        else if !highlighted {
            Spring {
                self.transform = .init(scaleX: 1, y: 1)
            }
        }
    }
    
    override func prepareForReuse() {
        paintingImageView.image = nil
        paintingImageView.frame = self.contentView.bounds
    }
    
    func configureWith(value vm: ImageViewModel) {
        paintingImageView.configureWith(viewModel: vm)
    }
}
