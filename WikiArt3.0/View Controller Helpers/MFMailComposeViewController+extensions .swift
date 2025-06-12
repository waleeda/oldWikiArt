//
//  MFMailComposeViewController+extensions .swift
//
//  Created by Waleed Azhar on 2019-08-15.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit
import MessageUI

extension MFMailComposeViewController {
    
    convenience init(toAddress: String) {
        self.init()
        setToRecipients([toAddress])
        setSubject("Customer Support Email")
    }
    
}
