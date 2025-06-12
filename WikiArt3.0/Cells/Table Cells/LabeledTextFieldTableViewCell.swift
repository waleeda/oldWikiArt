//
//  LabeledTextFieldTableViewCell.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-01.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

struct LabledTextFieldCellViewModel {
    let title: LabelViewModel
    let changed: (UITextField) -> ()

    init(_ title: String, _ changed: @escaping (UITextField) -> ()) {
        self.title = .init(title.uppercased(),
                           .preferredFont(forTextStyle: .subheadline),
                           .left,
                           .lightGray)
        self.changed = changed
    }
}

class LabeledTextFieldTableViewCell: UITableViewCell {

    @IBOutlet weak var textfield: UITextField!
    @IBOutlet weak var label: UILabel!
    var changed: (UITextField) -> () = { _ in }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        textfield.addTarget(self, action: #selector(textFieldDidChanged(_:)), for: .editingChanged)
    }
    
    @objc func textFieldDidChanged(_ textfield: UITextField) {
        changed(textfield)
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    func configure(_ viewModel: LabledTextFieldCellViewModel) {
        self.label.configure(viewModel.title)
        self.changed = viewModel.changed
        selectionStyle = .none
    }

}
