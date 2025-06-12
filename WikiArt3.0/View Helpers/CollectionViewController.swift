//
//  WATableViewController.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-01.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

class CollectionViewController: UICollectionViewController, ViewControllerRouter {
    
    var transition: Transition?
    
    public var loadingString = "Loading More"
    
    public var completeString = "All Content Loaded"
        
    private var loadingContainerView = UIView()
    
    private var loadingIndicatorView = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.white)
    
    private var loadingLabelView = UILabel()
    
    private var loadingContainerBottomConstraint = NSLayoutConstraint()
    
    private let loadingContainerBottomVisible: CGFloat = -8
    
    private let loadingContainerBottomHidden: CGFloat = 88
    
    private var stackView = UIStackView()
    
    private var isVisible = false
    
    private var loadingViewVisible: Bool {
        return loadingContainerBottomConstraint.constant == loadingContainerBottomVisible
    }

    var transitionDelegate: Transition? {
        get {
            return (self.presentingViewController as? ViewControllerRouter)?.transition
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupSubviews()
        setupConstraints()
    }
    
    private func setupSubviews() {
        
        loadingContainerView.translatesAutoresizingMaskIntoConstraints = false
        loadingIndicatorView.translatesAutoresizingMaskIntoConstraints = false
        loadingLabelView.translatesAutoresizingMaskIntoConstraints = false
        stackView.translatesAutoresizingMaskIntoConstraints = false
        
        view.addSubview(loadingContainerView)

        stackView.addArrangedSubview(loadingIndicatorView)
        stackView.addArrangedSubview(loadingLabelView)
        stackView.spacing = 8.0
        loadingContainerView.addSubview(stackView)
        
        loadingContainerView.backgroundColor = .black
        loadingContainerView.layer.cornerRadius = 14
        loadingIndicatorView.startAnimating()
        
        loadingLabelView.text = loadingString
        loadingLabelView.textColor = .white
        loadingLabelView.font = UIFont.systemFont(ofSize: 12.0)
    }
    
    private func setupConstraints() {
        loadingContainerView.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        loadingContainerView.widthAnchor.constraint(equalToConstant: 140).isActive = true
        loadingContainerBottomConstraint = loadingContainerView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: loadingContainerBottomHidden)
        loadingContainerBottomConstraint.isActive = true
        loadingContainerView.heightAnchor.constraint(equalToConstant: 28).isActive = true
        
        loadingIndicatorView.alpha = 0
        loadingLabelView.alpha = 0
        
        stackView.centerXAnchor.constraint(equalTo: loadingContainerView.centerXAnchor).isActive = true
        stackView.centerYAnchor.constraint(equalTo: loadingContainerView.centerYAnchor).isActive = true
    }
    
}


extension CollectionViewController {
    
    public func toggleLoadingView() {
        if loadingViewVisible {
            hideLoading()
        }
        else {
            showLoading()
        }
    }

    private func showLoading() {
        view.bringSubviewToFront(loadingContainerView)
        loadingContainerBottomConstraint.constant = loadingContainerBottomVisible
        isVisible = true

        Spring {
            self.loadingIndicatorView.alpha = 1
            self.loadingLabelView.alpha = 1
            self.view.setNeedsLayout()
        }
    }
    
    private func hideLoading() {
        view.bringSubviewToFront(loadingContainerView)
        loadingContainerBottomConstraint.constant = loadingContainerBottomHidden
        isVisible = false
        Spring {
            self.loadingLabelView.alpha = 0
            self.loadingIndicatorView.alpha = 0
            self.view.layoutIfNeeded()
        }
    }
}
