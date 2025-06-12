//
//  PaintingListFecher.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-12.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

enum ArtistPaintingListSort: Int {
    
    static let all = [name, date]
    
    case name, date
    
    var title: String {
        switch self {
        case .date:
            return NSLocalizedString("All Paintings by Date", comment: "")
        case .name:
            return NSLocalizedString("All Paintings by Name", comment: "")
        }
    }
}

final class ArtistPaintingListFectcher: PagedFetcher {
    typealias Model = [Painting]
    
    private(set) var page: Int = 0
 
    private(set) var isFetching: Bool = false
    
    private(set) var isDepleted: Bool = false
    
    var hasRemainingPages: Bool {
        return (page == 0) || allPaintingCount > loadedPaintingsCount
    }
    
    private(set) var artist: Artist
    
    private let sort: ArtistPaintingListSort
    
    private var allPaintingCount = 0
    
    private(set) var loadedPaintingsCount = 0
    
    init(artist: Artist, sort: ArtistPaintingListSort) {
        self.artist = artist
        self.sort = sort
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
        
        Current.apiService.getAllPainting(for: artist, sort: sort, page: page + 1)
        { [weak self] result in
            DispatchQueue.main.async
            { [weak self] in
                guard let self = self else { return }
                switch result
                {
                case .success(let paintingList):
                    if self.page == 0 {
                        self.allPaintingCount = paintingList.allPaintingsCount
                    }
                    self.loadedPaintingsCount += paintingList.paintings.count
                    self.page += 1
                    self.isDepleted = self.allPaintingCount <= self.loadedPaintingsCount
                    competion(.success(paintingList.paintings))
                case .failure:
                    competion(.failure(.error))
                }
                self.isFetching = false
            }
        }
    }
}


