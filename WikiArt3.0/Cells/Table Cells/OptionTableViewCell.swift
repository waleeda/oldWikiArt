//
//  OptionTableViewCell.swift
//  test2
//
//  Created by Waleed Azhar on 2019-10-22.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

final class OptionTableViewCell: UITableViewCell, ValueCell {
    static var defaultReusableId: String = String(describing: OptionTableViewCell.self)
    @IBOutlet weak var icon: UIImageView!
    @IBOutlet weak var title: UILabel!
    
    func configureWith(value: PaintingCategory) {
        icon.tintColor = .red
        icon.image = value.icon.image
        title.text = value.title
        accessoryType = value.hasSections ? .disclosureIndicator : .checkmark
        tintColor = .darkGray
    }
    
    func configureWith(value: PaintingSection) {
        icon.tintColor = .red
        icon.image = Icon.title.image
        title.text = value.title
        accessoryType = .checkmark
        tintColor = .darkGray
    }
    
    func configureWith(value: ArtistCategory) {
        icon.tintColor = .systemBlue
        icon.image = value.icon.image
        title.text = value.title
        accessoryType = value.hasSections ? .disclosureIndicator : .checkmark
        tintColor = .darkGray
    }
    
    func configureWith(value: ArtistsSection) {
        icon.tintColor = .systemBlue
        icon.image = Icon.title.image
        title.text = value.Title
        accessoryType = .checkmark
        tintColor = .darkGray
    }
}
