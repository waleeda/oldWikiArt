//
//  AppVersion.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-27.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

enum AppVersion: String {
    case one = "1.0"
    case two = "2.0"
    case threeOne = "3.0"
    case four = "4.0"
    case fourOne = "4.1"
    case fourOneOne = "4.1.1"
    case fourTwo = "4.2"
    case fourThree = "4.3"
    
    init?(_ versionString: String?){
        guard let versionString = versionString else {
            return nil
        }
        self.init(rawValue: versionString)
    }
}

extension AppVersion: Comparable {
    
    static func < (lhs: AppVersion, rhs: AppVersion) -> Bool {
        lhs.rawValue < rhs.rawValue
    }
    
}

extension AppVersion {
    
    static var bundleVersion: String? {
        return Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String
    }
    
    static var current: AppVersion? {
        guard let versionString = Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String else {
            return nil
        }
        return AppVersion(versionString)
    }
    
    static var lastLaunchVersion: AppVersion? {
        return AppVersion(Current.userDefaults.lastLaunchAppVersion)
    }
    
}
