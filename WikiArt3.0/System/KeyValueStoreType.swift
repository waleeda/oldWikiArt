
import Foundation

enum AppKeys: String {
    case seenAppRating = "com.WikiArt.KeyValueStoreType.hasSeenAppRating"
    case firstLaunch = "com.WikiArt.KeyValueStoreType.firstLaunch"
    case favorites = "com.WikiArt.KeyValueStoreType.favoritesStore"
    case lastLaunchVersion = "com.WikiArt.KeyValueStoreType.lastLaunchAppVersion"
}

protocol KeyValueStoreType: class {
    
    func set(_ value: Bool, forKey defaultName: String)
    func set(_ value: Int, forKey defaultName: String)
    func set(_ value: Any?, forKey defaultName: String)
    
    func bool(forKey defaultName: String) -> Bool
    func dictionary(forKey defaultName: String) -> [String: Any]?
    func integer(forKey defaultName: String) -> Int
    func object(forKey defaultName: String) -> Any?
    func string(forKey defaultName: String) -> String?
    func synchronize() -> Bool

    var hasSeenAppRating: Bool { get set }
    var firstLaunch: Bool { get set }
    var lastLaunchAppVersion: String? { get set }
}

extension KeyValueStoreType {
    
    var hasSeenAppRating: Bool {
        get {
            return self.bool(forKey: AppKeys.seenAppRating.rawValue)
        }
        set {
            self.set(newValue, forKey: AppKeys.seenAppRating.rawValue)
        }
    }
    
    var firstLaunch: Bool {
        get {
            return self.bool(forKey: AppKeys.firstLaunch.rawValue) == false
        }
        set {
            self.set(newValue == false, forKey: AppKeys.firstLaunch.rawValue)
        }
    }
    
    var lastLaunchAppVersion: String? {
        get {
            return self.string(forKey: AppKeys.lastLaunchVersion.rawValue)
        }
        set {
            self.set(newValue, forKey: AppKeys.lastLaunchVersion.rawValue)
        }
    }
}

extension UserDefaults: KeyValueStoreType { }

extension NSUbiquitousKeyValueStore: KeyValueStoreType {
    public func integer(forKey defaultName: String) -> Int {
        return Int(longLong(forKey: defaultName))
    }
    
    public func set(_ value: Int, forKey defaultName: String) {
        return self.set(Int64(value), forKey: defaultName)
    }
}

func encode<A:Codable>(_ content: A) -> Data? {
    let encoder = JSONEncoder()
    return try? encoder.encode(content)
}

func decode<A:Codable>(_ data: Data?) -> A? {
    guard let data = data else { return nil}
    let decode = JSONDecoder()
    return try? decode.decode(A.self, from: data)
}
