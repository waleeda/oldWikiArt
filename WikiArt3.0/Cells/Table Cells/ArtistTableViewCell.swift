//
//  ArtistTableViewCell.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-24.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

class ArtistTableViewCell: UITableViewCell, ValueCell {
    static var defaultReusableId: String = String(describing: ArtistTableViewCell.self)
    
    @IBOutlet weak var profileImage: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var nationLabel: UILabel!
    @IBOutlet weak var count: UILabel!
    
    func configureWith(value: Artist) {
        nameLabel.text = value.title
        count.text = value.totalWorksTitle?.capitalized
        nationLabel.text = String([value.nation, value.year])
        let imageModel = ImageViewModel(value)
        profileImage.configureWith(viewModel: imageModel)
    }
    
    
}
