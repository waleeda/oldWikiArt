//
//  Log.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-09.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

protocol Logger {
    var enabled: Bool { get set }
    func log(_ item: Any)
}

struct ConsoleLog: Logger {
    var enabled: Bool = true
    
    func log(_ item: Any) {
        guard enabled else { return }
        print(item)
    }
}

