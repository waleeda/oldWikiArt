//
//  MessageContentListModel.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-02.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit

enum DisplayMessage: String {
    case welcome = "~ Hello! Tap the search icon to begin. ~"
    case nothingFound = "~ Nothing here, try another query! ~"
    case noFav = "~ You don't have any favourites yet! ~"
    case error = "Something went ary!"
}

class MessageListModel: DataSource {
    
    let messageDisplay: DisplayMessage

    init(message: DisplayMessage) {
        self.messageDisplay = message
       // contentSections = [[ .line( .init(messageDisplay.rawValue, .body)) ]]
    }
    
}

//class DottedMessageListModel: ContentListModel {
//    
//    var data: [[Any]] = []
//    
//    let messageDisplay: DisplayMessage
//    
//    var contentSections: [[ContentRow]]
//    
//    init(message: DisplayMessage) {
//        self.messageDisplay = message
//        contentSections = [ [.line( .init("*", .init(), .none ,)),
//                             .line( .init(messageDisplay.rawValue, font: UIFont.boldSystemFont(ofSize: 18), textAlignment: .center)),
//                             .line( .init("*", font: UIFont.boldSystemFont(ofSize: 18), textColor: .red, textAlignment: .center))] ]
//    }
//    
//}
//
//class MessageButtonListModel: ContentListModel {
//    var data: [[Any]] = []
//    let messageDisplay: DisplayMessage
//    var contentSections: [[ContentRow]]
//    
//    init(message: DisplayMessage, buttonViewModel: ButtonCellViewModel) {
//        self.messageDisplay = message
//        contentSections = [[.button(buttonViewModel),
//                             .line( .init(messageDisplay.rawValue, font: UIFont.boldSystemFont(ofSize: 18), textAlignment: .center ) )]]
//    }
//    
//}

