//
//  PRLabel.swift
//  Mangazine
//
//  Created by Waleed Azhar on 10/11/17.
//  Copyright © 2017 Waleed Azhar. All rights reserved.
//

import UIKit

class Label: UILabel {
    
    init() {
        super.init(frame: .zero)
        setup()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    internal func setup() {
        translatesAutoresizingMaskIntoConstraints = false
    }
    
    var showTextShadow: Bool = true {
        didSet{
            setNeedsDisplay()
        }
    }
    var textShadowOffSet = CGSize(width:0, height: 2) {
        didSet{
            setNeedsDisplay()
        }
    }
    
    var textShadowColor = UIColor.init(white: 0, alpha: 0.5) {
        didSet{
            setNeedsDisplay()
        }
    }
    
    var textShadowBlur: CGFloat = 2 {
        didSet{
            setNeedsDisplay()
        }
    }
    
    override func drawText(in rect: CGRect) {
        guard showTextShadow == true else { return super.drawText(in: rect) }
        
        let myContext = UIGraphicsGetCurrentContext()!
        myContext.saveGState()
        myContext.setShadow(offset:textShadowOffSet, blur:textShadowBlur, color:textShadowColor.cgColor)
        super.drawText(in: rect)
        myContext.restoreGState();
    }
    
}
