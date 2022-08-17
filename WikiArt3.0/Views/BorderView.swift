//
//  PRBorderView.swift
//  Mangazine
//
//  Created by Waleed Azhar on 10/11/17.
//  Copyright © 2017 Waleed Azhar. All rights reserved.
//

import UIKit

class BorderView: View {
    
    private var border = CALayer()
    
    var borderWidth: CGFloat = 1.0 {
        didSet{
            border.borderWidth = borderWidth
        }
    }
    
    var borderAlpha: CGFloat = 0.2 {
        didSet{
            border.borderColor = UIColor.init(white: borderWidth, alpha: borderAlpha).cgColor
        }
    }
    
    var cornerRadius: CGFloat = 0 {
        didSet{
            layer.cornerRadius = cornerRadius
            border.cornerRadius = cornerRadius
        }
    }
    
    override func setup() {
        super.setup()
        
        backgroundColor = .clear
        layer.addSublayer(border)
        border.borderColor = UIColor.init(white: 1, alpha: borderAlpha).cgColor
        border.borderWidth = 1.0
        clipsToBounds = true
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        border.frame = bounds
    }

}
