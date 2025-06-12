import UIKit.UIViewController

internal protocol AlertViewController {
    
    func displayAlert(for error: Error, animated: Bool)
    
    func displayAlert(title: String, message: String?, animated: Bool)
    
}

internal extension AlertViewController where Self: UIViewController {
        
    func displayAlert(title: String, message: String?, animated: Bool = true) {
        let alert = UIAlertController(title: title , message: message, preferredStyle: .alert)
        let action = UIAlertAction(title: NSLocalizedString("Dismiss", comment: ""), style: UIAlertAction.Style.default) { [weak self] _ in
            DispatchQueue.main.async { [weak self]  in self?.presentedViewController?.dismiss(animated: animated, completion: nil) }
        }
        alert.addAction(action)
        alert.modalPresentationStyle = .overFullScreen
        
        DispatchQueue.main.async { [weak self] in self?.present(alert, animated: animated, completion: nil) }
    }
    
    func displayAlert(for error: Error, animated: Bool = true) {
        displayAlert(title: NSLocalizedString("Error", comment: ""), message: error.localizedDescription, animated: true)
    }
    
}
