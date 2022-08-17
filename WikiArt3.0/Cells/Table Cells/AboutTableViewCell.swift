//
//  AboutTableViewCell.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-15.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

struct AboutCellViewModel {
    let title, subtitle: String
    init(_ title: String, _ sub: String) {
        self.title = title
        self.subtitle = sub
    }
}

class AboutTableViewCell: UITableViewCell {

    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var subTitle: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        title.textColor = AppStyle.Color.jaguar
        //subTitle.textColor = AppStyle.Color.royalBlue
    }
        
    func configureWith(value vm: AboutCellViewModel) {
        title.text = vm.title
        title.textColor = AppStyle.Color.jaguar
        subTitle.text = vm.subtitle
    }
}
