//
//  Transitions.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-03.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

class SlideUpPresentAnimator: NSObject, UIViewControllerAnimatedTransitioning {
    
    var duration: TimeInterval = 0.4 ///Style.Animation.transtionDuration
    let dampingRatio: CGFloat = 0.7
    func transitionDuration(using transitionContext: UIViewControllerContextTransitioning?) -> TimeInterval {
        return duration
    }
    
    func animateTransition(using transitionContext: UIViewControllerContextTransitioning) {
        
        let toVc = transitionContext.viewController(forKey: .to)!
        //        let fromVc = transitionContext.viewController(forKey: .from)!
        let toView = transitionContext.view(forKey: .to)!
        //        let fromView = transitionContext.view(forKey: .to)!
        let finalFrame = transitionContext.finalFrame(for: toVc)
        let containerFrame = transitionContext.containerView.bounds
        let startingTo = CGRect(x: finalFrame.origin.x, y: containerFrame.height, width: finalFrame.width, height: finalFrame.height)
        toView.frame = startingTo
        transitionContext.containerView.addSubview(toView)
        
        
        let animations = {
            toView.frame = finalFrame
        }
        let completion: (UIViewAnimatingPosition) -> () = { _ in
                            let canceled = transitionContext.transitionWasCancelled
                            if canceled {
                                toView.removeFromSuperview()
                            }
                            transitionContext.completeTransition(!canceled)
                        }
        
        let animate = UIViewPropertyAnimator(duration: duration, dampingRatio: dampingRatio, animations: animations)
        animate.addCompletion(completion)
        animate.startAnimation()
        
    }
    
}

class SlideUpDismissAnimator: NSObject, UIViewControllerAnimatedTransitioning {
    
    var duration: TimeInterval = 0.4
    
    func transitionDuration(using transitionContext: UIViewControllerContextTransitioning?) -> TimeInterval {
        return duration
    }
    
    func animateTransition(using transitionContext: UIViewControllerContextTransitioning) {
        
        //let toVc = transitionContext.viewController(forKey: .to)!
        //let fromVc = transitionContext.viewController(forKey: .from)!
       // let toView = toVc.view!
        let fromView = transitionContext.view(forKey: .from)!
        //let finalFrame = transitionContext.finalFrame(for: toVc)
        let containerFrame = transitionContext.containerView.bounds
        let endingFrom = CGRect(x: fromView.frame.origin.x, y: containerFrame.height, width: fromView.frame.width, height: fromView.frame.height)
        transitionContext.containerView.addSubview(fromView)
        
        UIView.animate(withDuration: duration, animations:  {
            fromView.frame = endingFrom
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
