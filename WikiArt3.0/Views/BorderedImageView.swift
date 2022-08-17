//
//  PRBorderedImageView.swift
//  Mangazine
//
//  Created by Waleed Azhar on 10/11/17.
//  Copyright © 2017 Waleed Azhar. All rights reserved.
//

import UIKit

final class BorderedImageView: ShadowBorderView {
    
    private var imageView = ImageView()
    
    var image: UIImage? {
        get {
            return imageView.image
        }
        set {
            imageView.image = newValue
        }
    }
    
    var borderColer = UIColor.white
    
    var imageContentMode: UIView.ContentMode = .scaleToFill {
        didSet{
            imageView.contentMode = imageContentMode
        }
    }
    
    override func setup() {
        super.setup()
        
        addSubviewToContainer(imageView)
        backgroundColor = borderColer
    }
    
    override func setupConstraints() {
        super.setupConstraints()

        imageView.leftAnchor.constraint(equalTo: leftAnchor, constant: 2).isActive = true
        imageView.rightAnchor.constraint(equalTo: rightAnchor, constant: -2).isActive = true
        imageView.topAnchor.constraint(equalTo: topAnchor, constant: 2).isActive = true
        imageView.bottomAnchor.constraint(equalTo: bottomAnchor, constant: -2).isActive = true
    }
    
}
