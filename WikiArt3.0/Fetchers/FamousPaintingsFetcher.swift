//
//  CategorySectionsFetcher.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-12.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

final class FamousPaintingsFetcher : DataFetcher {
    
    typealias Model = [Painting]
    
    private(set) var isFetching: Bool = false
    
    private(set) var isDepleted: Bool = false
    
    private let artist: Artist
    
    init(artist: Artist) {
        self.artist = artist
    }
    
    func get(willBeginFetch: WillBegin,
             _ completion: @escaping (Result<Model, FetchOutcome>) -> Void)
    {
        guard isFetching == false else { completion(.failure(.busyFetching)); return }
        guard isDepleted == false else { completion(.failure(.depleted)); return }
        DispatchQueue.main.async { willBeginFetch?() }
        isFetching = true
        Current.apiService.getFamousPainting(for: artist)
        { result in
            DispatchQueue.main.async
            {
                switch result {
                case .success(let paintingList):
                    self.isDepleted = true
                    completion(.success(paintingList.paintings))
                case .failure:
                    completion(.failure(.error))
                }
                self.isFetching = false
            }
        }
    }
}
