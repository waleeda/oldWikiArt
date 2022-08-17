//
//  Created by Waleed Azhar on 2019-10-27.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit
import Firebase
import KiteSDK

open class AppLauncherTemplate {
    
    private let launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    unowned let appDelegate: AppDelegate
    
    init(appDelegate: AppDelegate, launchOptions: [UIApplication.LaunchOptionsKey: Any]?) {
        self.launchOptions = launchOptions
        self.appDelegate = appDelegate
    }
    
    func launch() {
        setupEnvironment()
        setupWindow()
        setupFavorites()
        updateLastLaunchVersion()
        themeApp()
        requestSupport()
        initFirebase()
        initKite()
    }
    
    func setupEnvironment() {
        Current = Environment()
    }
    
    func setupFavorites() {
        Current.favouritesService.restore()
        Current.favouritesService.restoreLegacy()
        Current.favouritesService.save()
    }
    
    func themeApp() {
        
    }
    
    func setupWindow() {
        let window = UIWindow()
        appDelegate.window = window
        let director = WindowDirector(window: window)
        director.construct()
    }
    
    func updateLastLaunchVersion() {
        Current.userDefaults.lastLaunchAppVersion = AppVersion.bundleVersion
    }
    
    func requestSupport() {
        guard let root = UIApplication.shared.keyWindow?.rootViewController as? TabBarController else { return }
        if Int.random(in: 1...5) == 5 {
            root.selectedIndex = 3
        }
    }
    
    func initFirebase() {
       FirebaseApp.configure()
    }
    
    func initKite() {
        OLKitePrintSDK.setAPIKey(Secrets.KiteKeys.publicKey, with: .live)
        OLKitePrintSDK.setURLScheme("WikiArtiOS")
    }
}
