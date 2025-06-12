//
//  SearchResultsFetcher.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-14.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

class ArtistSearchResultsFetcher : DataFetcher {
    typealias Model = [Artist]
    
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
        
        Current.apiService.getArtistsSearchResults(for: term)
        { [weak self] result in
            guard let self = self else { return }
            DispatchQueue.main.async
            {
                switch result
                {
                case .success(let artists):
                    self.isDepleted = true
                    completion(.success(artists.artists))
                case .failure:
                    completion(.failure(.error))
                }
                self.isFetching = false
            }
        }
    }
}
