//
//  PRImageView.swift
//  Mangazine
//
//  Created by Waleed Azhar on 10/11/17.
//  Copyright © 2017 Waleed Azhar. All rights reserved.
//

import UIKit

class ImageView: UIImageView {
    
    private var border = BorderView()
    
    init() {
        super.init(frame: .zero)
        setup()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setup()
    }
    
    override init(image: UIImage?) {
        super.init(image: image)
        setup()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    func setup() {
        addSubview(border)
        translatesAutoresizingMaskIntoConstraints = false
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        border.frame = bounds
    }

}
