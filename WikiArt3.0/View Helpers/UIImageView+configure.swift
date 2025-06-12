
//
//  File.swift
//  Gazebo-iOS
//
//  Created by Waleed Azhar on 2018-10-18.
//  Copyright © 2018 Waleed Azhar. All rights reserved.
//

import UIKit

extension UIImageView {
    
    public struct AssociatedKeys {
        static var ViewModel = "viewModel"
        static var DisplayedImage = "displayedImage"
    }
    
    var viewModel: ImageViewModel? {
        get { return objc_getAssociatedObject(self, &AssociatedKeys.ViewModel) as? ImageViewModel }
        set (value) { objc_setAssociatedObject(self, &AssociatedKeys.ViewModel, value, objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC) }
    }
    
    func configureWith(viewModel: ImageViewModel,
                   placeHolderCompletion: ((UIImage?) -> Void)? = nil,
                   completion: ((UIImage?) -> Void)? = nil) {
        self.viewModel = viewModel
        
        guard viewModel.imageUse == nil else {
            self.image = viewModel.imageUse
            return
        }
        
        self.image = nil
        
        let wrappedImageCompletion: (UIImage?) -> Void =
        { [weak self] image in

            if let loadedImage = image {
                DispatchQueue.main.async
                    { [weak self] in
                        self?.image = loadedImage 
                        completion?(image)
                }
            }
        }
        
        load(from: viewModel.image, completion: wrappedImageCompletion)
        
        guard viewModel.placeHolderImage == nil else {
               self.image = viewModel.placeHolderImage
               return
        }
        
        if let placeholder = viewModel.placeholder
        {
            let wrappedPlaceHolderCompletion: (UIImage?) -> Void =
            { [weak self] image in
                DispatchQueue.main.async
                { [weak self] in
                    guard
                        let loadedImage = image,
                        self?.image == nil
                    else { return }
                    self?.image = loadedImage
                    placeHolderCompletion?(image)
                }
            }
            load(from: placeholder, completion: wrappedPlaceHolderCompletion)
        }
    }
    
    private func load(from path: ImagePath, completion: ((UIImage?) -> Void)? = nil) {
       
        Current.imageServie.load(from: path)
        { [weak self] (cacheHit, recivedPath, image) in
            guard
                let vm = self?.viewModel,
                vm.image == recivedPath || vm.placeholder ?? "" == recivedPath
            else { return }
            completion?(image)
        }
    }
    
}
