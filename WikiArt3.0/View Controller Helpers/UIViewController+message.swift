import UIKit

let activityIndicatorStyle = UIActivityIndicatorView.Style.white

protocol ErrorMessageViewController {
    
}

fileprivate struct ErrorMessageAssociatedKeys {
    static var Message = "Message"
    static var LoadingContent = "LoadingContent"
}

extension ErrorMessageViewController where Self : UIViewController {
    
    var messageLabel: UILabel {
        get {
            if let label = objc_getAssociatedObject(self, &ErrorMessageAssociatedKeys.Message) as? UILabel {
                return label
            } else {
                let label = UILabel()
                self.view.addSubview(label)
                !label
                label.textColor = .white
                label.numberOfLines = 99
                label.font = UIFont.boldSystemFont(ofSize: 22)
                label * self.view
                objc_setAssociatedObject(self, &ErrorMessageAssociatedKeys.Message, label, objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC)
                return label
            }
        }
        
        set (value) {
            objc_setAssociatedObject(self, &ErrorMessageAssociatedKeys.Message, value, objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
        
    }
    
}
