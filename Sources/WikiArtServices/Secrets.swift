public enum Secrets {
    public static let isOSS = false
    public static let fieldReportEmail = "hello@email.com"
    
    public enum Api {
        
        public enum Client {
            public static let production = "deadbeef"
            public static let staging = "beefdead"
        }
        
        public enum Endpoint {
            public static let production = "www.wikiart.org"
            public static let staging = "www.wikiart.org"
        }
    }
    
    public enum BasicHTTPAuth {
        public static let username = "usr"
        public static let password = "pswd"
    }
    
    public enum CanvasPopKeys {
        public static let accessKey = ""
        public static let secretKey = ""
    }
    
    public enum KiteKeys {
        public static let secret = ""
        public static let publicKey = ""
    }
    
    
}
