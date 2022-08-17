//
//  String+Helpers.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-10.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

extension String {
    static let htmlOptions: [NSAttributedString.DocumentReadingOptionKey: Any] = [
        .documentType: NSAttributedString.DocumentType.html,
        .characterEncoding: String.Encoding.utf8.rawValue
    ]
    
    init(wikiString string: String) {
        if let data = string.data(using: .utf8),
           let attributedString = try? NSAttributedString(data: data, options: String.htmlOptions, documentAttributes: nil) {
            self = attributedString.string
        }
        else {
            self = string
        }
    }
}
extension Data {
    var html2AttributedString: NSAttributedString? {
        do {
            return try NSAttributedString(data: self, options: [.documentType: NSAttributedString.DocumentType.html, .characterEncoding: String.Encoding.utf8.rawValue], documentAttributes: nil)
        } catch {
            print("error:", error)
            return  nil
        }
    }
    var html2String: String {
        return html2AttributedString?.string ?? ""
    }
}

extension String {
    var html2AttributedString: NSAttributedString? {
        return Data(utf8).html2AttributedString
    }
    var html2String: String {
        return html2AttributedString?.string ?? ""
    }
    
    var hasUnicode: Bool {
        return self.range(of: #"&#\d+;"#,
                                options: .regularExpression) != nil
    }
}

extension String {
    init?(_ strings: [String?]) {
        let notNil:[String] = strings.filter { $0 != nil } as! [String]
        guard !notNil.isEmpty else {
            return nil
        }
        self.init(notNil)
    }
    
    init(_ strings: [String]) {
        var result = strings.reduce(""){ $0 + "\($1), " }
        _ = result.removeLast()
        _ = result.removeLast()
        self = result
    }
}
