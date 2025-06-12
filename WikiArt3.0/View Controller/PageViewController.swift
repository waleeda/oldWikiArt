//
//  PaintingDetailPageViewController.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-21.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

protocol PageViewControllerDelegate: class {
    func pageViewControllerDidDisplayPage(at index: Int)
}

final class PageViewController<VC: UIViewController>: UIPageViewController, UIPageViewControllerDataSource, ViewControllerRouter, UIPageViewControllerDelegate {
    
    var transition: Transition?
    private var contentViewControllers:[VC] = []    
    private var index = 0
    unowned var pageDelegate: PageViewControllerDelegate?
    
    private let left = "→"
    private let rigth = "←"
    private var both = "← →"
    
    var indicatorView: Label = {
        let label = Label()
        label.backgroundColor = .clear
        label.textColor = .lightText
        label.textAlignment = .center
        return label
    }()

    init(paintings: [Painting], index:Int) {
        self.index = index
        contentViewControllers = paintings.map { PaintingDetailsViewController($0) as! VC }
        super.init(transitionStyle: .scroll, navigationOrientation: .horizontal, options: nil)
        setViewControllers([contentViewControllers[index]], direction: .forward, animated: false, completion: nil)
        self.dataSource = self
        self.delegate = self
    }
    
    init(artists: [Artist], index:Int) {
        self.index = index
        self.contentViewControllers = artists.map { ArtistDetailsViewController($0) as! VC }
        super.init(transitionStyle: .scroll, navigationOrientation: .horizontal, options: nil)
        self.dataSource = self
        self.delegate = self
        setViewControllers([contentViewControllers[index]], direction: .forward, animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.clipsToBounds = false
        
        !indicatorView
        view + indicatorView
        indicatorView -* view
        indicatorView ^ (view.safeAreaLayoutGuide, -28)
        
        setIndicatorValue(index)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func pageViewController(_ pageViewController: UIPageViewController,
                            viewControllerBefore viewController: UIViewController) -> UIViewController? {
        guard let currIndex1 = (contentViewControllers.firstIndex(of: viewController as! VC)) else {
            return nil
        }
        let currIndex = currIndex1 - 1
        if currIndex < 0 {
            return nil
        }
        return contentViewControllers[currIndex % contentViewControllers.count]
    }

    func pageViewController(_ pageViewController: UIPageViewController,
                            viewControllerAfter viewController: UIViewController) -> UIViewController? {
        guard let currIndex1 = (contentViewControllers.firstIndex(of: viewController as! VC)) else {
            return nil
        }
        
        let currIndex = currIndex1 + 1
        if currIndex >= contentViewControllers.count {
            return nil
        }
        return contentViewControllers[currIndex % contentViewControllers.count]
    }
    
    func pageViewController(_ pageViewController: UIPageViewController,
                            didFinishAnimating finished: Bool,
                            previousViewControllers: [UIViewController],
                            transitionCompleted completed: Bool) {
        guard completed,
            let viewController = pageViewController.viewControllers?.first,
            let currIndex = (contentViewControllers.firstIndex(of: viewController as! VC))
        else { return }
        setIndicatorValue(currIndex)
        pageDelegate?.pageViewControllerDidDisplayPage(at: currIndex)
    }
    
    fileprivate func setIndicatorValue(_ currIndex: Int) {

        if contentViewControllers.count == 1 {
            indicatorView.text = ""
        }
        else if currIndex == contentViewControllers.count - 1 {
            indicatorView.text = rigth
        }
        else if currIndex == 0 {
            Spring { self.indicatorView.text = self.left }
        }
        else {
            indicatorView.text = both
        }

    }
}

extension PageViewController: Scrollable {
    
    func scrollToTop(animated: Bool) {
        viewControllers?.forEach { ($0 as? Scrollable)?.scrollToTop(animated: animated) }
    }
    
}


