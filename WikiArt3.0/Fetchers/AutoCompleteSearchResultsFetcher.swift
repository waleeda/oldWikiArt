//
//  SearchResultsFetcher.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-14.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

class AutoCompleteSearchResultsFetcher : DataFetcher {
    typealias Model = [SearchAutoComplete]
    
    private(set) var isFetching: Bool = false
    
    private(set) var isDepleted: Bool = false
    
    private var term: String
    
    init(term: String) {
        self.term = term
    }

    deinit {
        print("Bye BYe", term)
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
        
        Current.apiService.getAutoCompleteSearchResults(for: term)
        { [weak self] result in
            guard let self = self else { return }
            DispatchQueue.main.async
            {
                switch result
                {
                case .success(let autocomplete):
                    self.isDepleted = true
                    let result = autocomplete.filter {
                        guard let type = $0.type else { return false }
                        switch type {
                        case .paintingsCategory, .artistCategory: return true
                        default: return false
                        }
                    }
                    
                    completion(.success(result))
                case .failure:
                    completion(.failure(.error))
                }
                self.isFetching = false
            }
        }
    }
}
