//
//  ButtonTableViewCell.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-01.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

struct ButtonCellViewModel {
    let config: (UIButton) -> ()
    let pressed: (UIButton) -> ()
}

class ButtonTableViewCell: UITableViewCell, ValueCell {
    static var defaultReusableId: String = String(describing: ButtonTableViewCell.self)
    
    
    @IBOutlet weak var button: UIButton!
  
    var callBack: (UIButton) -> () = { _ in }
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    func configureWith(value viewModel: ButtonCellViewModel) {
        self.callBack = viewModel.pressed
        viewModel.config(button)
        selectionStyle = .none
    }
    
    @IBAction func pressed(_ sender: Any) {
       callBack(button)
    }
}
