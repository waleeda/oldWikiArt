//
//  LoadingIndicatorTableViewCell.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-01.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

struct LoadingCellViewModel {
    let animating: Bool
    let style: UIActivityIndicatorView.Style
    let backgroundColor: UIColor
    
    init(_ animating: Bool = true,
         _ style: UIActivityIndicatorView.Style = UIActivityIndicatorView.Style.gray,
         _ backgroundColor: UIColor = .white
        )
    {
        self.animating = animating
        self.style = style
        self.backgroundColor = backgroundColor
    }
}

final class LoadingIndicatorTableViewCell: UITableViewCell, ValueCell {
    static var defaultReusableId: String = String(describing: LoadingIndicatorTableViewCell.self)

    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
   
    override func awakeFromNib() {
        super.awakeFromNib()
        activityIndicator.startAnimating()
    }
    
    override func prepareForReuse() {
        activityIndicator.startAnimating()
    }
    
    func configureWith(value: LoadingCellViewModel){
        backgroundColor = value.backgroundColor
        value.animating ? activityIndicator.startAnimating() : activityIndicator.stopAnimating()
        activityIndicator.style = value.style
    }
}
