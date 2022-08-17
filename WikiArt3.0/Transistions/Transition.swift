//
//  ViewControllerTransition.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-02.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

class Transition: NSObject, UIViewControllerTransitioningDelegate {
    enum Direction { case dismiss, present }
    enum Kind { case modal(style: UIModalPresentationStyle, overParent: Bool), detail }
    enum Animator { case slideUp, slideLeft, pop(PopAnimatorConfiguration), zoom(ZoomAnimatorConfiguration) }
    enum Presentation { case standard, card(CardPresentationConfiguration?) }
    
    private var presentaionController: UIPresentationController?
    private let presentaion: Presentation
    let animated = Current.animationsEnabled
    private let animatorStyle: Animator
    let kind: Kind

    init(animatorStyle: Animator,
         kind: Kind = .detail,
         presentaion: Presentation = .standard)
    {
        self.animatorStyle = animatorStyle
        self.kind = kind
        self.presentaion = presentaion
    }
    deinit {
        print("dedee")
    }
    @objc func animationController(forPresented presented: UIViewController,
                                   presenting: UIViewController,
                                   source: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        
        return animatorStyle.presentAnimator
    }
    
    @objc func animationController(forDismissed dismissed: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        return animatorStyle.dismissAnimator
    }
    
    @objc func presentationController(forPresented presented: UIViewController,
                                      presenting: UIViewController?,
                                      source: UIViewController) -> UIPresentationController? {
        presentaionController = presentaion.presentationController(presentedViewController: presented, presenting: presenting)
        return self.presentaionController
    }
    
}

