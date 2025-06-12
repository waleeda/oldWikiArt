//
//  SearchResultsFetcher.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-14.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

class PaintingSearchResultsFetcher : DataFetcher {
    typealias Model = [Painting]
    
    private(set) var isFetching: Bool = false
    
    private(set) var isDepleted: Bool = false
    
    private var term: String
    
    init(term: String) {
        self.term = term
    }

    func get(willBeginFetch: WillBegin,
             _ completion: @escaping (Result<Model, FetchOutcome>) -> Void)
    {
        guard isFetching == false else { completion(.failure(.busyFetching)); return }
        guard isDepleted == false else { completion(.failure(.depleted)); return }
        
        DispatchQueue.main.async {
            willBeginFetch?()
        }
        
        isFetching = true
        
        Current.apiService.getPaintingSearchResults(for: term)
        { [weak self] result in
            guard let self = self else { return }
            DispatchQueue.main.async
            {
                switch result
                {
                case .success(let paintingsList):
                    self.isDepleted = true
                    completion(.success(paintingsList.paintings))
                case .failure:
                    completion(.failure(.error))
                }
                self.isFetching = false
            }
        }
    }
}
