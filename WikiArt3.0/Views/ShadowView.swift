//
//  PRShadowView.swift
//  Mangazine
//
//  Created by Waleed Azhar on 10/11/17.
//  Copyright © 2017 Waleed Azhar. All rights reserved.
//

import UIKit

struct ShadowViewConfiguration {
    let color: UIColor
    let offset: CGSize
    let radius: CGFloat
    let opacity: CGFloat
}

class ShadowView: View {

    internal let container = View()
    
    override func setup() {
        super.setup()
        
        clipsToBounds = false
        backgroundColor = .clear
        layer.shadowColor = UIColor.black.cgColor
        layer.shadowOffset = CGSize.init(width: 0, height: 0)
        layer.shadowRadius = 2
        layer.shadowOpacity = 0.6

        container.backgroundColor = .clear
        container.clipsToBounds = true
        super.addSubview(container)
    }
    
    override func setupConstraints() {
        container.leftAnchor.constraint(equalTo: leftAnchor).isActive = true
        container.rightAnchor.constraint(equalTo: rightAnchor).isActive = true
        container.topAnchor.constraint(equalTo: topAnchor).isActive = true
        container.bottomAnchor.constraint(equalTo: bottomAnchor).isActive = true
    }

    func addSubviewToContainer(_ view: UIView) {
        container.addSubview(view)
    }

}
