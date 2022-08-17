//
//  PRView.swift
//  Mangazine
//
//  Created by Waleed Azhar on 10/11/17.
//  Copyright © 2017 Waleed Azhar. All rights reserved.
//

import UIKit

class View: UIView {
    
    init() {
        super.init(frame: .zero)
        setup()
        setupConstraints()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
        setupConstraints()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("coded init not implemented" )
    }
    
    internal func setup(){
        translatesAutoresizingMaskIntoConstraints = false
    }
    
    internal func setupConstraints() {
        
    }

}
