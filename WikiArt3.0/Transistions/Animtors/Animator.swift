//
//  TransitionStyle.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-03.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit


extension Transition.Animator {
    var presentAnimator: UIViewControllerAnimatedTransitioning {
        switch self {
        case .slideUp:
            return SlideUpPresentAnimator()
        case .slideLeft:
            return SlideLeftPresentAnimator()
        case .pop(let source):
            return PopAnimator(configuration: source, direction: .present)
        case .zoom(let config):
            return ZoomAnimator(configuration: config, direction: .present)
        }
    }

    var dismissAnimator: UIViewControllerAnimatedTransitioning {
        
        switch self {
        case .slideUp:
            return SlideUpDismissAnimator()
        case .slideLeft:
            return SlideLeftDismissAnimator()
        case .pop(let source):
            return PopAnimator(configuration: source, direction: .dismiss)
        case .zoom(let config):
            return ZoomAnimator(configuration: config, direction: .dismiss)
        }
    }
}

public func Spring(_ duration: TimeInterval = 0.8,
                   _ dampingRatio:CGFloat = 0.5,
                   _ animations: @escaping () -> Void)  {
    if #available(iOS 10.0, *) {
        let animate = UIViewPropertyAnimator(duration: duration, dampingRatio: dampingRatio, animations: animations)
        animate.startAnimation()
    } else {
        // Fallback on earlier versions
    }
    
}

public func SpringCompletion(_ animations: @escaping () -> Void,
                             _ completion:  (() -> Void)? = nil)  {
    if #available(iOS 10.0, *) {
        let animate = UIViewPropertyAnimator(duration: 0.1, dampingRatio: 0.8, animations: animations)
        animate.addCompletion { (_) in
            completion?()
        }
        animate.startAnimation()
    } else {
        // Fallback on earlier versions
    }
    
}


extension Transition.Presentation {
    
    func presentationController(presentedViewController: UIViewController, presenting: UIViewController?) -> UIPresentationController? {
        switch  self {
        case .standard:
            return nil
        case .card(let configuration):
            return CardPresentationController(configuration: configuration!, presentedViewController: presentedViewController, presenting: presenting)
        }
    }
}
