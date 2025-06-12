//
//  CategorySectionsFetcher.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-12.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

class ArtistsCategorySectionsFetcher : DataFetcher {
    typealias Model = [ArtistsSection]
    
    private(set) var isFetching: Bool = false
    
    private(set) var isDepleted: Bool = false
    
    private var category: ArtistCategory
    
    init(category: ArtistCategory) {
        self.category = category
    }
    
    func get(willBeginFetch: WillBegin,
             _ completion: @escaping (Result<ArtistsCategorySectionsFetcher.Model, FetchOutcome>) -> Void)
    {
        guard isFetching == false else { completion(.failure(.busyFetching)); return }
        guard isDepleted == false else { completion(.failure(.depleted)); return }
        
        DispatchQueue.main.async {
            willBeginFetch?()
        }
        
        isFetching = true
        
        Current.apiService.getSections(for: category)
        { result in
            DispatchQueue.main.async
            {
                switch result
                {
                case .success(let sections):
                    self.isDepleted = true
                    completion(.success(sections.items))
                case .failure:
                    completion(.failure(.error))
                }
                self.isFetching = false
            }
        }
    }
}
