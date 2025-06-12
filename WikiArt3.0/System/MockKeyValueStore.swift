//
//  MockKeyValueStore.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-04.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

class MockKeyValueStore: KeyValueStoreType {
    
    func set(_ value: Bool, forKey defaultName: String) {
        
    }
    
    func set(_ value: Int, forKey defaultName: String) {
        
    }
    
    func set(_ value: Any?, forKey defaultName: String) {
        
    }
    
    func bool(forKey defaultName: String) -> Bool {
     
        return true
    }
    
    func dictionary(forKey defaultName: String) -> [String : Any]? {
        return [:]
    }
    
    func integer(forKey defaultName: String) -> Int {
        return -1
    }
    
    func object(forKey defaultName: String) -> Any? {
        return nil
    }
    
    func string(forKey defaultName: String) -> String? {
        return nil
    }
    
    func synchronize() -> Bool {
        return false
    }
    
    var hasSeenAppRating: Bool {
        get {
            return false
        }
    }
    

    
}
