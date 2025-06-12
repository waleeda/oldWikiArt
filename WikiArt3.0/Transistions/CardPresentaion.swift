//
//  Presentaion.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-04.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit
import simd

struct CardPresentationConfiguration {
    let rect: CGRect
    weak var sourceView: UIView?
    weak var superView: UIView?
}

class CardPresentationController: UIPresentationController {
    
    let heightInset: CGFloat = 40
    let widthInset: CGFloat = 0
    let configuration: CardPresentationConfiguration
    let shadow = UIVisualEffectView(effect: nil)
    var button: UIButton?

    init(configuration: CardPresentationConfiguration, presentedViewController: UIViewController, presenting presentingViewController: UIViewController?) {
        self.configuration = configuration
        super.init(presentedViewController: presentedViewController, presenting: presentingViewController)
    }
    
    override var frameOfPresentedViewInContainerView: CGRect {
        return configuration.rect
    }
    
    override func presentationTransitionWillBegin() {
        containerView?.addSubview(shadow)
       
        shadow.translatesAutoresizingMaskIntoConstraints = false
        shadow -*| containerView!
        self.presentedViewController.transitionCoordinator?.animate(alongsideTransition: { _ in
            self.shadow.effect = UIBlurEffect(style: .dark)
        }, completion: nil)
        
        let gesture1 = UIPanGestureRecognizer(target: self, action: #selector(panGesture))
        shadow.addGestureRecognizer(gesture1)
        let gesture2 = UITapGestureRecognizer(target: self, action: #selector(tapGesture))
        shadow.addGestureRecognizer(gesture2)
        
        if #available(iOS 13.0, *) {
            button = UIButton(type: .close)
        } else {
            button = .init(type: .system)
            button?.setTitle(NSLocalizedString("Close", comment: ""), for: .normal)
        }
        button?.tintColor = .white
        button?.translatesAutoresizingMaskIntoConstraints = false
        shadow.contentView.addSubview(button!)
        button?.leadingAnchor.constraint(equalToSystemSpacingAfter: shadow.contentView.safeAreaLayoutGuide.leadingAnchor, multiplier: 1.0).isActive = true
        button?.topAnchor.constraint(equalToSystemSpacingBelow: shadow.contentView.safeAreaLayoutGuide.topAnchor, multiplier: 1.0).isActive = true
        button?.addTarget(self, action: #selector(buttonTapper), for: .touchUpInside)
    }

    override func dismissalTransitionWillBegin() {
       super.dismissalTransitionWillBegin()
        self.presentedViewController.transitionCoordinator?.animate(alongsideTransition: { (_) in
            self.shadow.effect = nil
        }, completion:  nil)
    }
    
    override func dismissalTransitionDidEnd(_ completed: Bool) {
        super.dismissalTransitionDidEnd(true)
        var router = self.presentingViewController as? ViewControllerRouter
        router?.transition = nil
    }
    
    @objc func tapGesture(_ tap: UITapGestureRecognizer) {
        (presentedViewController as? Scrollable)?.scrollToTop(animated: true)
        presentedViewController.dismiss(animated: true, completion: nil)
    }
    
    @objc func buttonTapper() {
        (presentedViewController as? Scrollable)?.scrollToTop(animated: true)
        presentedViewController.dismiss(animated: true, completion: nil)
    }
    
    var original = CGPoint.zero
    var originalLoc = CGPoint.zero
    var frame: CGRect = .zero
    var oriFrame : CGRect = .zero
    @objc func panGesture(_ pan: UIPanGestureRecognizer) {
        switch pan.state {
        case .began:
            (presentedViewController as? Scrollable)?.scrollToTop(animated: false)
            original = presentedViewController.view.frame.origin
            originalLoc = pan.location(in: shadow)
            oriFrame = presentedView?.bounds ?? presentedViewController.view.bounds
            presentedView?.clipsToBounds = false
        case .changed:
            let translation = pan.translation(in: shadow)
            let location = pan.location(in: shadow)
            let presentedRect = oriFrame
        
            let origin = SIMD2<Double>(arrayLiteral: 0, 0)
            let final = SIMD2<Double>(arrayLiteral:Double(shadow.frame.width), Double(shadow.frame.height))
            let current = SIMD2<Double>(arrayLiteral:Double(location.x), Double(location.y))
            let smoothStep = simd_smoothstep(origin, final, current)

            let cadidate: CGFloat = 1.0 - CGFloat(smoothStep.y)
            let min = ((configuration.sourceView?.frame.width ?? 0)/presentedRect.width)
            
            let scaleY: CGFloat = max(cadidate, min*0.8)
            shadow.alpha = 1.0 - CGFloat(smoothStep.y) - 0.5
            let width = scaleY * presentedRect.size.width
            let height = scaleY * presentedRect.size.height
            
            frame = CGRect(x: original.x + translation.x + (location.x * (1.0 - scaleY)),
                           y: original.y + translation.y,
                           width: width,
                           height: height)
            presentedView?.frame = frame
        case .ended, .cancelled, .failed:
            let translation = pan.translation(in: shadow).y
            let endPoint = shadow.frame.maxY
            let percent = translation / endPoint
            if percent > 0.25 {
                presentedViewController.dismiss(animated: true, completion: nil)
            }
            else {
                Spring {
                    self.presentedView?.frame = self.frameOfPresentedViewInContainerView
                    self.shadow.alpha = 1
                    self.button?.alpha = 1
                }
            }
        default:
            return
        }
    }
}
