//
//  SlideHorizontalAnimators.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-03.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

class SlideLeftPresentAnimator: NSObject, UIViewControllerAnimatedTransitioning {
    
    var duration: TimeInterval = AppStyle.Animation.transtionDuration
    
    func transitionDuration(using transitionContext: UIViewControllerContextTransitioning?) -> TimeInterval {
        return duration
    }
    
    func animateTransition(using transitionContext: UIViewControllerContextTransitioning) {
        
        let toVc = transitionContext.viewController(forKey: .to)!
        let fromVc = transitionContext.viewController(forKey: .from)!
        let toView = toVc.view!
        let fromView = fromVc.view!
        
        let finalToFrame = transitionContext.finalFrame(for: toVc)

        let containerFrame = transitionContext.containerView.bounds
        
        let endingFrom = fromView.frame.offsetBy(dx: -containerFrame.width, dy: 0)
        
        let startingTo = CGRect(x: containerFrame.width, y: 0, width: containerFrame.width, height: containerFrame.height)
        toView.frame = startingTo
        transitionContext.containerView.addSubview(toView)
        let originalFromFrame = fromView.frame
        UIView.animate(withDuration: duration, animations:  {
            toView.frame = finalToFrame
            fromView.frame = endingFrom
        },
                       completion: { finished in
                        let canceled = transitionContext.transitionWasCancelled
                        if canceled {
                            toView.removeFromSuperview()
                        }
                        fromView.frame = originalFromFrame
                        transitionContext.completeTransition(!canceled)
        })
        
    }
    
}

class SlideLeftDismissAnimator: NSObject, UIViewControllerAnimatedTransitioning {
    
    var duration: TimeInterval = AppStyle.Animation.transtionDuration
    
    func transitionDuration(using transitionContext: UIViewControllerContextTransitioning?) -> TimeInterval {
        return duration
    }
    
    func animateTransition(using transitionContext: UIViewControllerContextTransitioning) {
        
        let toVc = transitionContext.viewController(forKey: .to)!
        let fromVc = transitionContext.viewController(forKey: .from)!
        let toView = toVc.view!
        let fromView = fromVc.view!
        let finalFrame = transitionContext.finalFrame(for: toVc)
        let containerFrame = transitionContext.containerView.bounds
        let endingFrom = CGRect(x: containerFrame.width, y: 0, width: containerFrame.width, height: containerFrame.height)
       
        transitionContext.containerView.addSubview(toView)
        toView.frame = containerFrame.offsetBy(dx: -containerFrame.width, dy: 0)
        toView.bringSubviewToFront(fromView)
        
        UIView.animate(withDuration: duration, animations:  {
            fromView.frame = endingFrom
            toView.frame = finalFrame
        },
                       completion: { finished in
                        let success = !transitionContext.transitionWasCancelled
                        if success {
                            fromView.removeFromSuperview()
                        }
                        transitionContext.completeTransition(success)
        })
        
    }
    
}
