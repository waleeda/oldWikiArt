//
//  Created by Waleed Azhar on 2019-08-05.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit
import KiteSDK

var Current = Environment()

@UIApplicationMain
 class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    
    private let cacheRemovalTimer = Timer.scheduledTimer(withTimeInterval: 60*60, repeats: true)
    { timer in
        Service.session.configuration.urlCache?.removeAllCachedResponses()
    }
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        let launcher = AppLauncherTemplate(appDelegate: self, launchOptions: launchOptions)
        launcher.launch()
        return true
    }
    
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        return OLKitePrintSDK.handleUrlCallBack(url)
    }
}
