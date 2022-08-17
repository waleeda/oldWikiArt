//
//  Button.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-03.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

extension UIButton {
    convenience init(target: Any?, selector: Selector) {
        self.init(type: .system)
        setTitle("Back", for: .normal)
        addTarget(target, action: selector, for: .touchUpInside)
        
        sizeToFit()
    }
}
