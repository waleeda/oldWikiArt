// TitleTableViewCell
//  TitleTableViewCell.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-10.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

struct TitleCellViewModel {
    let title, subtitle: String
    let subHeading: String?
    
    init(_ title: String, _ sub: String) {
        self.title = title
        self.subtitle = sub
        self.subHeading = nil
    }
    
    init(_ painting: Painting) {
            title = painting.title
            subtitle = painting.artistName
            subHeading = painting.year
        }
}

final class TitleTableViewCell: UITableViewCell, ValueCell {
    static var defaultReusableId: String = String(describing: TitleTableViewCell.self)
    
    @IBOutlet weak var artist: UILabel!
    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var subTitle: UILabel!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        artist.text = nil
        title.text = nil
        subTitle.text = nil
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        
    }
    
    override func setHighlighted(_ highlighted: Bool, animated: Bool) {
        //super.setHighlighted(highlighted, animated: animated)
        if highlighted {
            Spring {
                self.transform = .init(scaleX: 0.9, y: 0.9)
            }
        }
        else if !highlighted {
            Spring {
                self.transform = .init(scaleX: 1, y: 1)
            }
        }
    }
    
    func configureWith(value vm: TitleCellViewModel) {
        artist.text = vm.subtitle
        title.text = vm.title
        subTitle.text = vm.subHeading
    }

}

