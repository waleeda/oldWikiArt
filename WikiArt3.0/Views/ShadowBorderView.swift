//
//  PRShadowBorderView.swift
//  Mangazine
//
//  Created by Waleed Azhar on 10/11/17.
//  Copyright © 2017 Waleed Azhar. All rights reserved.
//

import UIKit

class ShadowBorderView: ShadowView {
    
    private var border = BorderView()
    
    var borderWidth: CGFloat = 1.0 {
        didSet{
            border.borderWidth = borderWidth
        }
    }
    
    var borderAlpha: CGFloat = 0.5 {
        didSet{
            border.borderAlpha = 0.5
        }
    }
    
    var cornerRadius: CGFloat = 0 {
        didSet{
            border.cornerRadius = cornerRadius
            container.layer.cornerRadius = cornerRadius
        }
    }
    
    override func setup() {
        super.setup()
        super.addSubview(border)
    }
    
    override func addSubview(_ view: UIView) {
        fatalError(" cant add subview to shadowview use addSubviewToContainer")
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        border.frame = bounds
    }

}
