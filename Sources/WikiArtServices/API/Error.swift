import Foundation

public struct BlankError: Error {
    public var localizedDescription: String = "Blank Error"
}

public struct BadServiceError: Error {
    public var localizedDescription: String = "BadServiceError"
}

public struct DataTaskFailure: Error {
    public var localizedDescription: String = "Could Not Download Data"
}


public struct JSONDecodeFailure: Error {
    public var localizedDescription: String = "Could Not Decode Data"    
}

public struct PaymentError: Error {
    public var localizedDescription: String = "Unable to Process Payment"
}

public struct ShippingAddressError: Error {
    public var localizedDescription: String = "Missing a shipping address"
}

public struct NoSectionsForCategory: Error {
    public var localizedDescription: String = "No Sections for this Category"
}

public struct UnsupportedCategory: Error {
    public var localizedDescription: String = "No paintings for Unsupported Category"
}

public struct ErrorRegister: Codable {
    
    public struct ErrorEmail: Codable {
        public let Email: [String]
    }
    
    public let success: Bool?
    public let error: ErrorEmail?
    
}
