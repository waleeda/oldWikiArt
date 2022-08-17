//
//  ZoomInAnimator.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-27.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

struct ZoomAnimatorConfiguration {
    weak var sourceView: UIView?
    weak var superView: UIView?
}

final class ZoomAnimator: NSObject, UIViewControllerAnimatedTransitioning {
    
    let duration: TimeInterval = 0.4
    let dampingRatio: CGFloat = 0.8
    let configuration: ZoomAnimatorConfiguration
    let direction: Transition.Direction
    
    init(configuration: ZoomAnimatorConfiguration, direction: Transition.Direction) {
        self.configuration = configuration
        self.direction = direction
        super.init()
    }
    
    func transitionDuration(using transitionContext: UIViewControllerContextTransitioning?) -> TimeInterval {
        return duration
    }
    
    func animateTransition(using transitionContext: UIViewControllerContextTransitioning) {
        
        var animations: () -> Void
        var finalFrame: CGRect
        var completion: (UIViewAnimatingPosition) -> ()
        
        switch direction {
        case .dismiss:
            let fromVc = transitionContext.viewController(forKey: .from)!
            let fromView = transitionContext.view(forKey: .from)!
            let finalSubstituteRect = transitionContext.finalFrame(for: fromVc)
            transitionContext.containerView.addSubview(fromView)
            let origanllStartingFrame = configuration.sourceView?.layer.presentation()?.frame ?? configuration.sourceView?.frame ?? fromView.frame
            let neturalRect  = configuration.superView?.convert(origanllStartingFrame, to: fromView) ?? finalSubstituteRect
            let endingFrom = fromView.convert(neturalRect, to: transitionContext.containerView)
        
            animations = {
                fromView.transform = .identity
                fromView.frame = endingFrom
            }
            
            completion =
            { [weak self] _ in
                 
                let success = !transitionContext.transitionWasCancelled
                self?.configuration.sourceView?.alpha = success ? 1 : 0
                if success {
                    fromView.removeFromSuperview()
                }
               
                transitionContext.completeTransition(success)
            }
            
        case .present:
            let toVc = transitionContext.viewController(forKey: .to)!
            let toView = toVc.view!
            let fromVc = transitionContext.viewController(forKey: .from)!
            let fromView = fromVc.view!
            finalFrame = transitionContext.finalFrame(for: toVc)
            let origanllStartingFrame = configuration.sourceView?.layer.presentation()?.frame ?? configuration.sourceView?.frame ?? fromView.frame
            let neturalRect  = configuration.superView?.convert(origanllStartingFrame, to: fromView) ?? finalFrame
            let startingTo = fromView.convert(neturalRect, to: transitionContext.containerView)
            toView.frame = startingTo
            toVc.viewWillAppear(true)
            transitionContext.containerView.addSubview(toView)
            self.configuration.sourceView?.alpha = 0

            animations = {
                toView.frame = finalFrame
            }
            
            completion = { _ in
                let canceled = transitionContext.transitionWasCancelled
                if canceled {
                    self.configuration.sourceView?.alpha = 1
                    toView.removeFromSuperview()
                }
                transitionContext.completeTransition(!canceled)
            }
        }
 
       
        
        let animate = UIViewPropertyAnimator(duration: duration, dampingRatio: dampingRatio, animations: animations)
        animate.addCompletion(completion)
        animate.startAnimation()
    }
    
}
