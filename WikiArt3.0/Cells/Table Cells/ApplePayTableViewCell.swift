//
//  ApplePayTableViewCell.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-11.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit
import PassKit

struct ApplePayCellViewModel {
    let pressed: () -> Void
    
    init(_ pressed: @escaping () -> Void){
        self.pressed = pressed
    }
}



class ApplePayTableViewCell: UITableViewCell {
    
    private let buyButton = PKPaymentButton.init(paymentButtonType: .buy, paymentButtonStyle: .black)
    private var pressed: () -> Void
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        pressed = {}
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        self + buyButton
        backgroundColor = .white
        !buyButton
        buyButton |- (safeAreaLayoutGuide, 16)
        buyButton -| (safeAreaLayoutGuide, -16)
        buyButton ^ (safeAreaLayoutGuide, 8)
        buyButton !^ (safeAreaLayoutGuide, -8)
        //buyButton | 48
        buyButton.addTarget(self, action: #selector(tapped), for: .touchUpInside)
        //buyButton.isUserInteractionEnabled = false
    }
    
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    @objc func tapped() {
        pressed()
    }
    
    func configureWith(value vm: ApplePayCellViewModel) {
        pressed = vm.pressed
    }
}
