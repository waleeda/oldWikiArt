//
//  Cell.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-21.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

public protocol ValueCell: class {
  static var defaultReusableId: String { get }
}
