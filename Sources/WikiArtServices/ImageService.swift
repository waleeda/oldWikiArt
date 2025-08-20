//
//  ImageService.swift
//
//  Created by Waleed Azhar on 2019-08-10.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
#if canImport(UIKit)
import UIKit

public typealias ImageCompletion = (Bool, ImagePath, UIImage?) -> Void

public protocol ImageServiceType {
    func load(from path: String, completion: @escaping ImageCompletion)
    func preload(path: String)
    func preload(paths: [String])
    func cachedImaged(from path: String) -> UIImage?
}

public final class ImageService: ImageServiceType {
    
    static let imageCache = NSCache<NSString, UIImage>()
    
    static let session: URLSession = {
        return URLSession(configuration: .ephemeral)
    }()
    
    public func cachedImaged(from path: String) -> UIImage? {
        return ImageService.imageCache.object(forKey: .init(string: path))
    }
    
    public func load(from path: String, completion: @escaping ImageCompletion) {
        if path == "BlankArtist", let image = UIImage(named: path) {
            completion(true, path, image)
            return
        }
        else if let image = ImageService.imageCache.object(forKey: .init(string: path)) {
            completion(true, path, image)
            return
        }
        
        guard let url = NSURL(string: path) else { return }
        self.makeRequest(url, path, completion)
    }
    
    public func preload(path: String) {
        guard let url = NSURL(string: path) else { return }
        self.makeRequest(url, path, nil)
    }
    
    public func preload(paths: [String]) {
        paths.forEach {
            self.preload(path: $0)
        }
    }

    fileprivate func makeRequest(_ url: NSURL, _ path: String, _ completion: ((Bool, ImagePath, UIImage?)->Void)?) {
             
        let request = NSMutableURLRequest(url: url as URL)
        let task = ImageService.session.dataTask(with: request as URLRequest)
        { (data, _, _) in
            guard
                let data = data,
                let image = UIImage(data: data)
                else
            {
                completion?(false, path, nil)
                return
            }
            
            ImageService.imageCache.setObject(image, forKey: .init(string: path))
            completion?(false, path, image)
        }
        
        task.resume()
        return
    }
}
#endif
