//
//  PRRoundLabel.swift
//  Mangazine
//
//  Created by Waleed Azhar on 10/11/17.
//  Copyright © 2017 Waleed Azhar. All rights reserved.
//

import UIKit

class PRRoundLabel: Label {
   
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func setup(){
        super.setup()
        clipsToBounds = true
        textAlignment = .center
        backgroundColor = .white
        showTextShadow = false
    }
    
    override func layoutSubviews() {
        layer.cornerRadius = bounds.height/2
        super.layoutSubviews()
        
    }
}
