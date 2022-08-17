//
//  CategorySectionsFetcher.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-12.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

final class ArtistDetailsFetcher : DataFetcher {
    
    typealias Model = ArtistDetails
    
    private(set) var isFetching: Bool = false
    
    private(set) var isDepleted: Bool = false
    
    private let path: String?
    
    init(path: String?) {
        self.path = path
    }
    
    convenience init(artist: Artist) {
        self.init(path: artist.artistURL)
    }
    
    func get(willBeginFetch: WillBegin,
             _ completion: @escaping (Result<ArtistDetailsFetcher.Model, FetchOutcome>) -> Void)
    {
        guard isFetching == false else { completion(.failure(.busyFetching)); return }
        guard isDepleted == false else { completion(.failure(.depleted)); return }
        DispatchQueue.main.async { willBeginFetch?() }
        isFetching = true
        Current.apiService.getArtistDetails(for: path)
        { result in
            DispatchQueue.main.async
            {
                switch result {
                case .success(let details):
                    self.isDepleted = true
                    completion(.success(details))
                case .failure:
                    completion(.failure(.error))
                }
                self.isFetching = false
            }
        }
    }
}
