//
//  CartTableViewCell.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-09-15.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

final class CartTableViewCell: UITableViewCell, ValueCell {
    static var defaultReusableId: String = String(describing: CartTableViewCell.self)
    
    @IBOutlet weak var painting: UIImageView!
    @IBOutlet weak var price: UILabel!
    @IBOutlet weak var title: UILabel!
    
    func configureWith(value vm: SearchAutoComplete, term: String) {
        title.attributedText = NSMutableAttributedString(title: vm.label, term: term)
        painting.configureWith(viewModel: ImageViewModel(vm))
        price.text = vm.description
    }
    
    func configureWith(value vm: Painting) {
        title.text = vm.title
        painting.configureWith(viewModel: ImageViewModel(vm))
        price.text = vm.artistName
    }
    
    func configureWith(value vm: Artist) {
        title.text = vm.title
        painting.configureWith(viewModel: ImageViewModel(vm))
        price.text = vm.totalWorksTitle
    }
}

extension NSMutableAttributedString {
    
    convenience init(title: String, term: String) {
        let string = NSString(string: title.lowercased())
        self.init(string: title)
        let range = string.range(of: term.lowercased(), options: .forcedOrdering)
        addAttributes([NSAttributedString.Key.foregroundColor : UIColor.systemBlue], range: range)
    }
    
}
