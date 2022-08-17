//
//  TabBarController.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-03.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

final class NavigationController: UINavigationController, UINavigationControllerDelegate, ViewControllerRouter, Scrollable {
    
    var transition: Transition?
    
    init(rootViewController: UIViewController, transtionStyle style: Transition.Animator? = nil) {
        super.init(rootViewController: rootViewController)
        if let style = style {
            delegate = self
            transition = Transition(animatorStyle: style)
        }
    }
    
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationBar.prefersLargeTitles = true
    }
    
    func scrollToTop(animated: Bool) {
        viewControllers.forEach {
            ($0 as? Scrollable)?.scrollToTop(animated: animated)
        }
    }

    func navigationController(_ navigationController: UINavigationController,
                              animationControllerFor operation: UINavigationController.Operation,
                              from fromVC: UIViewController,
                              to toVC: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        switch operation {
        case .push:
            let priorityTransition = (fromVC as? ViewControllerRouter)?.transition ?? self.transition
            return priorityTransition?.animationController(forPresented: toVC, presenting: fromVC, source: self)
        case .pop:
            let priorityTransition = (toVC as? ViewControllerRouter)?.transition ?? self.transition
            return priorityTransition?.animationController(forDismissed: fromVC)
        case .none:
            return nil
        @unknown default:
            return nil
        }
    }

}
