//
//  ManagedObjectProtocol.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-29.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import CoreData

protocol ManagedObjectCodable: class {
    associatedtype ModelType: Codable
    init(model: ModelType, context: NSManagedObjectContext)
}

