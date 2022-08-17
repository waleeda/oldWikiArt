//
//  PaintingListFecher.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-12.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

final class ArtistsListFectcher: PagedFetcher {
    typealias Model = [Artist]
    
    private(set) var page: Int = 0
 
    private(set) var isFetching: Bool = false
    
    private(set) var isDepleted: Bool = false
    
    var hasRemainingPages: Bool {
        return (page == 0) || allArtistsCount > loadedArtistsCount
    }
    
    private(set) var category: ArtistCategory
    private(set) var section: ArtistsSection?
    
    private var allArtistsCount = 0
    private(set) var loadedArtistsCount = 0
    
    init(category: ArtistCategory, section: ArtistsSection?) {
        self.category = category
        self.section = section
    }
    
    func nextPage(willBeginFetch: WillBegin,
                  competion: @escaping (Result<Model,FetchOutcome>) -> Void)
    {
        guard isFetching == false else { competion(.failure(.busyFetching)); return }
        guard hasRemainingPages else { competion(.failure(.depleted)); return }
        guard !isDepleted else { competion(.failure(.depleted)); return }
        
        DispatchQueue.main.async {
            willBeginFetch?()
        }
        
        isFetching = true
        
        Current.apiService.getArtists(for: category, in: section, page: page + 1)
        { [weak self] result in
            DispatchQueue.main.async
            { [weak self] in
                guard let self = self else { return }
                switch result
                {
                case .success(let artistsList):
                    if self.page == 0 {
                        self.allArtistsCount = artistsList.allArtistsCount
                    }
                    self.loadedArtistsCount += artistsList.artists.count
                    self.page += 1
                    self.isDepleted = self.allArtistsCount <= self.loadedArtistsCount
                    competion(.success(artistsList.artists))
                case .failure:
                    competion(.failure(.error))
                }
                self.isFetching = false
            }
        }
    }
}


