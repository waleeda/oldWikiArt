
import Foundation

/**
 A type that knows the location of a wikiart
 */
public protocol ServerConfigType {
    var apiBaseUrl: URL { get }
    var environment: EnvironmentType { get }
}

public func == (lhs: ServerConfigType, rhs: ServerConfigType) -> Bool {
    return
        type(of: lhs) == type(of: rhs) &&
            lhs.apiBaseUrl == rhs.apiBaseUrl &&
            lhs.environment == rhs.environment
}

public enum EnvironmentType: String {
    
    public static let allCases: [EnvironmentType] = [.production, .staging]
    case production = "Production"
    case staging = "Staging"
}

public struct ServerConfig: ServerConfigType {
    
    public fileprivate(set) var apiBaseUrl: URL
    public fileprivate(set) var environment: EnvironmentType
    
    public static let production: ServerConfigType = ServerConfig(
        apiBaseUrl: URL(string: "https://\(Secrets.Api.Endpoint.production)")!,
        environment: EnvironmentType.production
    )
    
    public static let staging: ServerConfigType = ServerConfig(
        apiBaseUrl: URL(string: "https://\(Secrets.Api.Endpoint.staging)")!,
        environment: EnvironmentType.staging
    )
    
    public init(apiBaseUrl: URL,
                environment: EnvironmentType = .production) {
        self.apiBaseUrl = apiBaseUrl
        self.environment = environment
    }
    
    public static func config(for environment: EnvironmentType) -> ServerConfigType {
        switch environment {
        case .staging:
            return ServerConfig.staging
        case .production:
            return ServerConfig.production
        }
    }
}
