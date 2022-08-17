//
//  ColumnTableViewCell.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-11.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

struct ColumnCellViewModel {
    enum Style {
        case heading, row
    }

    let selectedColor: UIColor = .clear
    let backgroundColor: UIColor
    let accessory: UITableViewCell.AccessoryType
    let tint: UIColor = AppStyle.Color.checkMark

    let main: LabelViewModel
    let secondary: LabelViewModel
    let style: Style
    
    init( _ main: String?,
          _ secondary: String?,
          _ style: Style,
          _ backgroundColor: UIColor = .clear,
          _ accessory: UITableViewCell.AccessoryType = .none)
    {
        self.style = style
        self.backgroundColor = backgroundColor
        self.accessory = accessory
        switch style {
        case .heading:
            self.main = .init(main?.uppercased(),
                              .systemFont(ofSize: 18, weight: .semibold),
                              .left,
                              .darkText)
            self.secondary = .init(secondary?.uppercased(),
                                   .systemFont(ofSize: 18, weight: .semibold),
                                   .left,
                                   .white)
        case .row:
  
            self.main = .init(main,
                              .systemFont(ofSize: 18, weight: .semibold),
                              .left,
                              .lightGray)
            self.secondary = .init(secondary,
                                   .systemFont(ofSize: 18, weight: .regular),
                              .left,
                              .white)
        }
    }

}

final class ColumnTableViewCell: UITableViewCell, ValueCell {
    
    static var defaultReusableId: String = String(describing: ColumnTableViewCell.self)
    
    @IBOutlet weak var mainLabel: UILabel!
    @IBOutlet weak var secondary: UILabel!

    var tint: UIColor = .darkGray
    var bgc: UIColor = .darkGray

    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected && (isSelected == false), animated: animated)
        tintColor = isSelected ?  tint : .lightGray
        //backgroundColor = isSelected ? .darkGray : bgc
    }
    
    func configureWith(value vm: ColumnCellViewModel) {
        
        tint = vm.tint
        tintColor = .lightGray
        backgroundColor = vm.backgroundColor
       // bgc = vm.backgroundColor
        //perferredAccessoryType = vm.accessory
        accessoryType = vm.accessory
        let selectBackgroundView = UIView()
        selectBackgroundView.backgroundColor = vm.selectedColor
        self.selectedBackgroundView? = selectBackgroundView
        
        mainLabel.configure(vm.main)
        secondary.configure(vm.secondary)
    }
    
}
