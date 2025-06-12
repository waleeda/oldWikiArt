//
//  LabelViewModel.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-11.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit

struct LabelViewModel {
    enum Style {
        case centeredHeading, lightHeading, heading, subHead, body, italic, caption1, caption2
    }
    
    let text: String?
    let font: UIFont
    let textAlignment: NSTextAlignment
    let textColor: UIColor
    
    init(_ text: String?,
         _ font: UIFont,
         _ textAlignment: NSTextAlignment,
         _ textColor: UIColor
        )
    {
        self.text = text
        self.font = font
        self.textAlignment = textAlignment
        self.textColor = textColor
    }
    
    init(_ text: String?, _ style: Style) {
        switch style {
        case .centeredHeading:
            self.init(text,
                      .boldSystemFont(ofSize: 18),
                      .center,
                      .darkText)
        case .lightHeading:
            self.init(text,
                      .systemFont(ofSize: 18, weight: .semibold),
                      .left,
                      .lightGray)
        case .heading:
            self.init(text,
                      .boldSystemFont(ofSize: 18),
                      .left,
                      .darkText)
        case .subHead:
            self.init(text,
                      .systemFont(ofSize: 18, weight: .semibold),
                      .left,
                      .darkText)
        case .body:
            self.init(text,
                      .systemFont(ofSize: 16),
                      .left,
                      .darkText)
        case .caption1:
            self.init(text,
                      .italicSystemFont(ofSize: 14),
                      .left,
                      .darkText)
        case .caption2:
            self.init(text,
                      .italicSystemFont(ofSize: 14),
                      .left,
                      .darkText)
        case .italic:
            self.init(text,
                      .italicSystemFont(ofSize: 14),
                      .left,
                      .darkText)
        }
    }
}

extension UILabel {
    
    func configure(_ vm: LabelViewModel) {
        font = vm.font
        textAlignment = vm.textAlignment
        textColor = vm.textColor
        text = vm.text
    }
}
