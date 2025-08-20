//
//  ServiceType.swift
//  Poiesis
//
//  Created by Waleed Azhar on 2019-08-02.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
#if canImport(UIKit)
import UIKit
#endif

public typealias PaintingListCompletion = (Result<PaintingList, Error>) -> Void
public typealias PaintingSectionCompletion = (Result<[PaintingSection], Error>) -> Void
public typealias PaintingDetailsCompletion = (Result<PaintingDetails, Error>) -> Void
public typealias ArtistDetailsCompletion = (Result<ArtistDetails, Error>) -> Void
public typealias ArtistSectionsCompletion = (Result<ArtistSections, Error>) -> Void
public typealias ArtistsListCompletion = (Result<ArtistsList, Error>) -> Void
public typealias AutoCompleteCompletion = (Result<[SearchAutoComplete], Error>) -> Void

public protocol ServiceType {
    var appId: String { get }
    var serverConfig: ServerConfigType { get }
    var language: String { get }
    var currency: String { get }
    var buildVersion: String { get }
    
    func paintings(for category: PaintingCategory, page: Int, competion: @escaping PaintingListCompletion)
    func sections(for category: PaintingCategory, competion: @escaping PaintingSectionCompletion)
    func getPaintingDetails(for painting: Painting, competion: @escaping PaintingDetailsCompletion)
    func getRelatedPainting(for painting: Painting, competion: @escaping PaintingListCompletion)
    func getFamousPainting(for artist: Artist, competion: @escaping PaintingListCompletion)
    
    func getAllPainting(for artist: Artist, sort:ArtistPaintingListSort, page: Int, competion: @escaping PaintingListCompletion)
    func getArtistDetails(for path: String?, competion: @escaping ArtistDetailsCompletion)
    func getSections(for artistCategory: ArtistCategory, competion: @escaping ArtistSectionsCompletion)
    func getArtists(for artistCategory: ArtistCategory, in artistSection: ArtistsSection?, page: Int, competion: @escaping ArtistsListCompletion)
    
    func getPaintingSearchResults(for term: String, competion: @escaping PaintingListCompletion)
    func getArtistsSearchResults(for term: String, competion: @escaping ArtistsListCompletion)
    func getAutoCompleteSearchResults(for term: String, competion: @escaping AutoCompleteCompletion)
    
    func getPaintingsList(for type: SearchAutoComplete.ResultType?, page: Int, competion: @escaping PaintingListCompletion)
    func getArtistsList(for type: SearchAutoComplete.ResultType?, page: Int, competion: @escaping ArtistsListCompletion)
    
}

/// Headers & Request generators
public extension ServiceType where Self == Service {
    
    /**
     Prepares a URL request to be sent to the server.
     
     - parameter originalRequest: The request that should be prepared.
     - parameter query:           Additional query params that should be attached to the request.
     
     - returns: A new URL request that is properly configured for the server.
     */
    func preparedRequest(forRequest originalRequest: URLRequest, query: [String: Any] = [:]) -> URLRequest {
            
            var request = originalRequest
            guard let URL = request.url else {
                return originalRequest
            }
            
            var headers = self.defaultHeaders
            let method = request.httpMethod?.uppercased()
            var components = URLComponents(url: URL, resolvingAgainstBaseURL: false)!
            var queryItems = components.queryItems ?? []
            queryItems.append(contentsOf: self.defaultQueryParams.map(URLQueryItem.init(name:value:)))
            
            if method == .some("POST") || method == .some("PUT") {
                if request.httpBody == nil {
                    headers["Content-Type"] = "application/json; charset=utf-8"
                    request.httpBody = try? JSONSerialization.data(withJSONObject: query, options: [])
                }
            } else {
                queryItems.append(
                    contentsOf: query
                        .flatMap(queryComponents)
                        .map(URLQueryItem.init(name:value:))
                )
            }
            
            components.queryItems = queryItems.sorted { $0.name < $1.name }
            request.url = components.url
            
            let currentHeaders = request.allHTTPHeaderFields ?? [:]
            request.allHTTPHeaderFields = currentHeaders.withAllValuesFrom(headers)
            
            return request
    }
    
    /**
     Prepares a request to be sent to the server.
     
     - parameter URL:    The URL to turn into a request and prepare.
     - parameter method: The HTTP verb to use for the request.
     - parameter query:  Additional query params that should be attached to the request.
     
     - returns: A new URL request that is properly configured for the server.
     */
    func preparedRequest(forURL url: URL, method: Method = .GET, query: [String: Any] = [:]) -> URLRequest {
            
            var request = URLRequest(url: url)
            request.httpMethod = method.rawValue
            return self.preparedRequest(forRequest: request, query: query)
    }
    
    fileprivate var defaultHeaders: [String: String] {
        var headers: [String: String] = [:]
        headers["Accept-Language"] = self.language
        headers["WikiArt-App-Id"] = self.appId
        headers["WikiArt-iOS-App"] = self.buildVersion
        headers["User-Agent"] = Self.userAgent
        
        return headers
    }
    
    static var userAgent: String {
        
        let executable = Bundle.main.infoDictionary?["CFBundleExecutable"] as? String
        let bundleIdentifier = Bundle.main.infoDictionary?["CFBundleIdentifier"] as? String
        let app: String = executable ?? bundleIdentifier ?? "Gazebo"
        let bundleVersion: String = (Bundle.main.infoDictionary?["CFBundleVersion"] as? String) ?? "1"
        let model = UIDevice.current.model
        let systemVersion = UIDevice.current.systemVersion
        let scale = UIScreen.main.scale
        
        return "\(app)/\(bundleVersion) (\(model); iOS \(systemVersion) Scale/\(scale))"
    }
    
    fileprivate var defaultQueryParams: [String: String] {
        let query: [String: String] = [:]
        
        return query
    }
    
    fileprivate func queryComponents(_ key: String, _ value: Any) -> [(String, String)] {
        var components: [(String, String)] = []
        
        if let dictionary = value as? [String: Any] {
            for (nestedKey, value) in dictionary {
                components += queryComponents("\(key)[\(nestedKey)]", value)
            }
        } else if let array = value as? [Any] {
            for value in array {
                components += queryComponents("\(key)[]", value)
            }
        } else {
            components.append((key, String(describing: value)))
        }
        
        return components
    }
    
}

public extension Dictionary {
    
    /**
     Merges `self` with `other`, but all values from `other` trump the values in `self`.
     
     - parameter other: Another dictionary.
     
     - returns: A merged dictionary.
     */
    func withAllValuesFrom(_ other: Dictionary) -> Dictionary {
        var result = self
        other.forEach { result[$0] = $1 }
        return result
    }
    
}
