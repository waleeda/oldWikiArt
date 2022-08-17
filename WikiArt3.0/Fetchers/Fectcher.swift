//
//  Fectcher.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-12.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation

typealias WillBegin = (() -> Void)?

enum FetchOutcome: Error {
    case error, stale, busyFetching, depleted
}

protocol Fetcher: class {
    associatedtype Model
    var isFetching: Bool { get }
    var isDepleted: Bool { get }
}

protocol DataFetcher: Fetcher {
    func get(willBeginFetch: WillBegin, _ completion: @escaping (Result<Model,FetchOutcome>) -> Void)
}

protocol PagedFetcher: Fetcher {
    var page: Int { get }
    func nextPage(willBeginFetch: WillBegin, competion: @escaping (Result<Model,FetchOutcome>) -> Void)
}

