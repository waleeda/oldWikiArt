//
//  WikiArt.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-09.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

protocol WindowBuilder {
    var window: UIWindow? { get }
    
    func buildRootViewController()
//    func buildPaintingsViewController()
//    func buildArtistsViewController()
    func buildWindow()
}

protocol WindowDirectorProtocol {
    var builder: WindowBuilder { get }
    init(window: UIWindow?)
    func construct()
}

struct iPhoneWindowBuilder: WindowBuilder {
    
    private(set)weak var window: UIWindow?
    
    func buildRootViewController() {
        let artists = ArtistsViewController()

        artists.tabBarItem = .init(title: NSLocalizedString("Artists", comment: ""), image: UIImage(named: "Artists"), selectedImage: nil)
        let artistsNavigation = NavigationController(rootViewController: artists)
        
        let paintings = PaintingCollectionViewController(category: .featured)
        paintings.tabBarItem = .init(title: NSLocalizedString("Paintings", comment: ""), image: UIImage(named: "Paintings"), selectedImage: nil)
        let paintingsNavigation = NavigationController(rootViewController: paintings)

        let search = SearchResultsViewController()
        search.tabBarItem = .init(title: NSLocalizedString("Search", comment: ""), image: UIImage(named: "Search"), selectedImage: nil)
        let searchNavigation = NavigationController(rootViewController: search)
        
        let support = SupportViewController()
        support.tabBarItem = .init(title: NSLocalizedString("Support", comment: ""), image: UIImage(named: "support"), selectedImage: nil)

        let tapBar = TabBarController()
        tapBar.setViewControllers([paintingsNavigation, artistsNavigation, searchNavigation, support], animated: false)
        window?.rootViewController = tapBar
    }
    
    func buildWindow() {
        window?.makeKeyAndVisible()
    }
    
}


struct iPadWindowBuilder: WindowBuilder {
    
    weak var window: UIWindow?

    func buildRootViewController() {
        
        let artists = ArtistsViewController()
        artists.tabBarItem = .init(title: NSLocalizedString("Artists", comment: ""), image: UIImage(named: "Artists"), selectedImage: nil)
        let artistsNavigation = NavigationController(rootViewController: artists)
        
        let paintings = PaintingCollectionViewController(category: .featured)
        paintings.tabBarItem = .init(title: NSLocalizedString("Paintings", comment: ""), image: UIImage(named: "Paintings"), selectedImage: nil)
        let paintingsNavigation = NavigationController(rootViewController: paintings)
        
        let search = SearchResultsViewController()
        search.tabBarItem = .init(title: NSLocalizedString("Search", comment: ""), image: UIImage(named: "Search"), selectedImage: nil)
        let searchNavigation = NavigationController(rootViewController: search)
        
        let support = SupportViewController()
        support.tabBarItem = .init(title: NSLocalizedString("Support", comment: ""), image: UIImage(named: "support"), selectedImage: nil)
        
        let tapBar = TabBarController()
        tapBar.setViewControllers([paintingsNavigation, artistsNavigation, searchNavigation, support], animated: false)
        window?.rootViewController = tapBar
    }
    
    func buildWindow() {
        window?.makeKeyAndVisible()
    }
}

struct WindowDirector: WindowDirectorProtocol {
    
    let builder: WindowBuilder
    
    init(window: UIWindow?) {
        let isIpad = Current.device.userInterfaceIdiom == .pad
        self.builder = isIpad ? iPadWindowBuilder(window: window) : iPhoneWindowBuilder(window: window)
    }
    
    func construct() {
        builder.buildRootViewController()
        builder.buildWindow()
    }
    
}
