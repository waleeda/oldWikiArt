//
//  PopAnimator.swift
//
//  Created by Waleed Azhar on 2019-08-14.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

struct PopAnimatorConfiguration {
    unowned var sourceView: UIView
    unowned var superView: UIView
}

final class PopAnimator: NSObject, UIViewControllerAnimatedTransitioning {
    
    let duration: TimeInterval = 0.4
    let dampingRatio: CGFloat = 0.8
    let configuration: PopAnimatorConfiguration
    let direction: Transition.Direction
    
    init(configuration: PopAnimatorConfiguration, direction: Transition.Direction) {
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
            let fromView = transitionContext.view(forKey: .from)!
            transitionContext.containerView.addSubview(fromView)
            
            let originalStartingFrame = configuration.sourceView.frame
            let neturalRect  = configuration.superView.convert(originalStartingFrame, to: fromView)
            let endingFrom = fromView.convert(neturalRect, to: transitionContext.containerView)
    
            let fromFrame = fromView.frame
            
            let finalFromFrame: CGRect = .init(origin: endingFrom.origin,
                                               size: .init(width: endingFrom.width, height: endingFrom.width*fromFrame.size.aspectRatioHeight))
        
            let heightRatio = (finalFromFrame.size.height / fromFrame.size.height)
            let widthRatio = (finalFromFrame.size.width / fromFrame.size.width)
        
            let scale = CGAffineTransform(scaleX: widthRatio, y: heightRatio)

            let translation = CGAffineTransform(translationX: (endingFrom.origin.x - fromView.frame.origin.x) - ((1 - widthRatio)*fromFrame.size.width)/2.0,
                                                y: endingFrom.origin.y - fromView.frame.origin.y - ((1 - heightRatio)*fromFrame.size.height)/2.0)

            configuration.sourceView.removeFromSuperview()
            transitionContext.containerView.addSubview(configuration.sourceView)
            configuration.sourceView.frame = endingFrom
            
             
            let fromFrame2 = endingFrom
             
            let finalFromFrame2: CGRect = .init(origin: fromView.frame.origin,
                                                size: .init(width: fromFrame.width, height: fromFrame.width * configuration.sourceView.frame.size.aspectRatioHeight))
             
            let heightRatioConfig = (finalFromFrame2.size.height / fromFrame2.size.height)
            let widthRatioConfig = (finalFromFrame2.size.width / fromFrame2.size.width)
            let scaleConfig = CGAffineTransform(scaleX: widthRatioConfig, y: heightRatioConfig)
            let translationConfig = CGAffineTransform(translationX: (finalFromFrame2.origin.x - fromFrame2.origin.x) - ((1 - widthRatioConfig)*fromFrame2.size.width)/2.0,
                                                       y: finalFromFrame2.origin.y - fromFrame2.origin.y - ((1 - widthRatioConfig)*fromFrame2.size.height)/2.0)
            configuration.sourceView.transform = scaleConfig.concatenating(translationConfig)
            
            
            configuration.sourceView.alpha = 0
            fromView.alpha = 1
            
            animations = {
                fromView.transform = scale.concatenating(translation)
                self.configuration.sourceView.transform = .identity
                self.configuration.sourceView.alpha = 1
                fromView.alpha = 0
            }
            
            completion =
            { _ in
                self.configuration.sourceView.alpha = 1
                self.configuration.sourceView.removeFromSuperview()
                self.configuration.superView.addSubview(self.configuration.sourceView)
                self.configuration.sourceView.transform = .identity
                self.configuration.sourceView.frame = originalStartingFrame
                
                let success = !transitionContext.transitionWasCancelled
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
            
            toVc.viewWillAppear(true)
            transitionContext.containerView.addSubview(toView)
            toView.frame = finalFrame
            
            let originalStartingFrame = configuration.sourceView.frame
            let neturalRect = configuration.superView.convert(originalStartingFrame, to: fromView)
            let startingTo_ = fromView.convert(neturalRect, to: transitionContext.containerView)
            
            let startingTo: CGRect = .init(origin: startingTo_.origin,
                                            size: .init(width: startingTo_.width, height: startingTo_.width*finalFrame.size.aspectRatioHeight))
            
            let scaleRatio = (startingTo.size.height / finalFrame.size.height)
                  
            let scale = CGAffineTransform(scaleX: scaleRatio, y: scaleRatio)
            
            let xDiff: CGFloat = startingTo.origin.x - finalFrame.origin.x
            let scaleXDiff:CGFloat = ((1 - scaleRatio) * finalFrame.size.width) / 2.0
            let transX =  xDiff - scaleXDiff
            
            let yDiff = max(startingTo.origin.y, finalFrame.origin.y) -  min(startingTo.origin.y, finalFrame.origin.y)
            let scaleYDiff = ((1 - scaleRatio) * finalFrame.height) / 2.0
            
            let translation = CGAffineTransform(translationX: transX,
                                                          y:  yDiff - scaleYDiff)

            toView.transform = scale.concatenating(translation)
            
            
            configuration.sourceView.removeFromSuperview()
            transitionContext.containerView.addSubview(configuration.sourceView)
            
            configuration.sourceView.frame = startingTo_
            configuration.sourceView.alpha = 1
            
            let fromFrame = startingTo_
            
            let finalFromFrame: CGRect = .init(origin: finalFrame.origin,
                                               size: .init(width: finalFrame.width, height: finalFrame.width * configuration.sourceView.frame.size.aspectRatioHeight))
            
            let heightRatioConfig = (finalFromFrame.size.height / fromFrame.size.height)
            let widthRatioConfig = (finalFromFrame.size.width / fromFrame.size.width)
            let scaleConfig = CGAffineTransform(scaleX: widthRatioConfig, y: heightRatioConfig)
            let translationConfig = CGAffineTransform(translationX: (finalFromFrame.origin.x - fromFrame.origin.x) - ((1 - widthRatioConfig)*fromFrame.size.width)/2.0,
                                                      y: min(fromFrame.origin.y, finalFromFrame.origin.y) -  max(fromFrame.origin.y, finalFromFrame.origin.y) - ((1 - widthRatioConfig)*fromFrame.size.height)/2.0)
            
            toView.alpha = 0
                
            animations = {
                toView.transform = .identity
                self.configuration.sourceView.transform = self.configuration.sourceView.transform.concatenating(scaleConfig.concatenating(translationConfig))
                toView.alpha = 1
                self.configuration.sourceView.alpha = 0
            }
            
            completion = { _ in
                let canceled = transitionContext.transitionWasCancelled
                
                self.configuration.sourceView.removeFromSuperview()
            self.configuration.superView.addSubview(self.configuration.sourceView)
                self.configuration.sourceView.transform = .identity
                self.configuration.sourceView.frame = originalStartingFrame
                
                if canceled {
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
