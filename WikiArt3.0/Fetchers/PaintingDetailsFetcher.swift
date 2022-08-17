//
//  CategorySectionsFetcher.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-12.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

final class PaintingDetailsFetcher : DataFetcher {
    
    typealias Model = PaintingDetails
    
    private(set) var isFetching: Bool = false
    
    private(set) var isDepleted: Bool = false
    
    private let painting: Painting
    
    init(painting: Painting) {
        self.painting = painting
    }
    
    func get(willBeginFetch: WillBegin,
             _ completion: @escaping (Result<PaintingDetailsFetcher.Model, FetchOutcome>) -> Void)
    {
        guard isFetching == false else { completion(.failure(.busyFetching)); return }
        guard isDepleted == false else { completion(.failure(.depleted)); return }
        DispatchQueue.main.async { willBeginFetch?() }
        isFetching = true
        Current.apiService.getPaintingDetails(for: painting)
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
