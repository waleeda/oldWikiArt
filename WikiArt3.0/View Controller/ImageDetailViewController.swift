//
//  ImageDetailViewController.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-14.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

class ImageDetailViewController: ViewController, UIScrollViewDelegate {

    private var scrollView = UIScrollView()
    private var imageView = UIImageView()
    private var centerX: NSLayoutConstraint?
    private var imageModel: ImageViewModel
    
    init(imageModel: ImageViewModel) {
        self.imageModel = imageModel
        
        super.init()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        scrollView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleToFill
        imageView.clipsToBounds = true
        scrollView.delegate = self
        scrollView.maximumZoomScale = 2.0
        scrollView.minimumZoomScale = 1.0
        scrollView.backgroundColor = .clear
        view.backgroundColor = .black
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        scrollView.addSubview(imageView)
        let intialFrame = CGRect(origin: view.frame.origin,
                                 size: .init(width: view.frame.width, height: view.frame.width/imageModel.width * imageModel.height))
        scrollView.frame = intialFrame
        imageView.frame = intialFrame
        view.addSubview(scrollView)
        imageView.configureWith(viewModel: imageModel)
        addTapGesture()
        self.setupConstraints()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()

        if imageView.frame.width < view.frame.width {
            let offset = CGPoint(x: -(view.center.x - imageView.center.x), y: 0)
            scrollView.setContentOffset(offset, animated: false)
            scrollView.contentSize = imageView.frame.size
        }
        else if imageView.frame.width >= view.frame.width {
            let offset = CGPoint(x: (imageView.frame.width/2) - (view.frame.width/2), y: 0)
            scrollView.setContentOffset(offset, animated: false)
            scrollView.contentSize = imageView.frame.size
        }
    }
    
    func viewForZooming(in scrollView: UIScrollView) -> UIView? {
        return imageView
    }
    
    func scrollViewShouldScrollToTop(_ scrollView: UIScrollView) -> Bool {
        return false
    }
    
    internal override func setupConstraints() {
        scrollView | view
        scrollView - view
        scrollView * view
        if (view.frame.width * (imageModel.height/imageModel.width)) > view.frame.height  {
            imageView.widthAnchor.constraint(equalTo: scrollView.widthAnchor).isActive = true
            imageView.heightAnchor.constraint(equalTo: imageView.widthAnchor, multiplier: (imageModel.height/imageModel.width)).isActive = true
        }
        else {
            imageView.heightAnchor.constraint(equalTo: scrollView.heightAnchor).isActive = true
            imageView.widthAnchor.constraint(equalTo: imageView.heightAnchor, multiplier: (imageModel.width/imageModel.height)).isActive = true
        }
    }
    
    private func addTapGesture() {
        let tap = UITapGestureRecognizer(target: self, action: #selector(didTap(tap:)))
        imageView.isUserInteractionEnabled = true
        imageView.addGestureRecognizer(tap)
    }
    
    @objc func didTap(tap: UITapGestureRecognizer) {
        scrollView.showsHorizontalScrollIndicator = false
        scrollView.showsVerticalScrollIndicator = false
        view.backgroundColor = .clear
        UIView.animate(withDuration: 0.02,
                       animations: { self.scrollView.zoomScale = 1 })
        { _ in
            self.dismiss(animated: true, completion: nil)
        }
    }
}

