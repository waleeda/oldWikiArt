//
//  Envirnment.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-02.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit
/**
 A collection of **all** global variables and singletons that the app wants access to.
 */
struct Environment {

    /// A type that exposes endpoints for fetching Gazebo data.
    let apiService: ServiceType
    
    /// A user defaults key-value store. Default value is `NSUserDefaults.standard`.
    let userDefaults: KeyValueStoreType
    
    /// A ubiquitous key-value store. Default value is `NSUbiquitousKeyValueStore.default`.
    public let ubiquitousStore: KeyValueStoreType
    
    let animationsEnabled: Bool
    
    var logger: Logger
    
    let imageServie: ImageServiceType
    
    let defaultPaintingSize: Painting.Size
    
    let defaultPlaceholderPaintingSize: Painting.Size
    
    let application: UIApplication
    
    let device: UIDevice
    
    let language: Language
    
    let layoutService: LayoutService
    
    let paddingService: LayoutPaddingServiceProtocol
    
    let favouritesService: Favorites
    
    init(apiService: ServiceType = Service(),
         userDefaults: KeyValueStoreType = UserDefaults.standard,
         ubiquitousStore: KeyValueStoreType = NSUbiquitousKeyValueStore.default,
         animationsEnabled: Bool = true,
         logger: Logger = ConsoleLog(enabled: true),
         imageService: ImageServiceType = ImageService(),
         defaultPaintingSize: Painting.Size = Painting.Size.large,
         defaultPlaceholderPaintingSize: Painting.Size = Painting.Size.medium,
         application: UIApplication = UIApplication.shared,
         device: UIDevice = UIDevice.current,
         layoutService: LayoutService = PadLayoutService(),
         paddingService: LayoutPaddingServiceProtocol = LayoutPaddingService(),
         favouritesService: Favorites = Favorites()
        )
    {
        self.apiService = apiService
        self.userDefaults = userDefaults
        self.logger = logger
        self.animationsEnabled = animationsEnabled
        self.imageServie = imageService
        
        self.defaultPlaceholderPaintingSize = defaultPlaceholderPaintingSize
        self.application = application
        self.ubiquitousStore = ubiquitousStore
        self.device = device
        self.language = Language(rawValue: NSLocale.current.languageCode ?? "") ?? .en
        let isIpad = device.userInterfaceIdiom == .pad
        self.defaultPaintingSize = isIpad ? Painting.Size.original: defaultPaintingSize
        self.layoutService = isIpad ? layoutService : PhoneLayoutService()
        self.paddingService = paddingService
        self.favouritesService = favouritesService
    }
    
}

