//
//  LineTableViewCell.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-01.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

struct LineCellViewModel {
    
    let line: LabelViewModel
    let accessory: UITableViewCell.AccessoryType
    let backgroundColor: UIColor
    
    init(_ line: LabelViewModel,
         _ accessory: UITableViewCell.AccessoryType = .none,
         _ backgroundColor: UIColor = .white)
    {
        self.line = line
        self.accessory = accessory
        self.backgroundColor = backgroundColor
    }
    
    init(_ text: String?,
         _ lineStyle: LabelViewModel.Style,
         _ accessory: UITableViewCell.AccessoryType = .none,
         _ backgroundColor: UIColor = .white)
    {
        self.line = .init(text, lineStyle)
        self.accessory = accessory
        self.backgroundColor = backgroundColor
    }
    
    init(_ text: String?, _ style: LabelViewModel.Style) {
        self.backgroundColor = .white
        self.accessory = .none
        self.line = .init(text, style)
    }
}

final class LineTableViewCell: UITableViewCell, ValueCell {
    static var defaultReusableId: String = String(describing: LineTableViewCell.self)

    @IBOutlet weak var lineLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        lineLabel.text = nil
        backgroundColor = .white
        accessoryType = .none
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {}
    override func setHighlighted(_ highlighted: Bool, animated: Bool) {}
    
    func configureWith(value vm: PaintingSection) {
        lineLabel.configure(.init(vm.title.uppercased(), .boldSystemFont(ofSize: 18), .center, .black))
        accessoryType = .none
        backgroundColor = .white
    }
    
    func configureWith(value vm: PaintingCategory) {
        lineLabel.configure(.init(vm.title.uppercased(), .boldSystemFont(ofSize: 18), .center, .black))
        accessoryType = vm.hasSections ? .disclosureIndicator : .none
        backgroundColor = .white
    }
    
    func configureWith(value viewModel: LineCellViewModel) {
        lineLabel.configure(viewModel.line)
        accessoryType = viewModel.accessory
        backgroundColor = viewModel.backgroundColor
    }
}
