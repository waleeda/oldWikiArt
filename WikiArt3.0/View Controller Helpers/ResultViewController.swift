import UIKit.UIViewController

internal protocol ResultViewController {
    func resultValue<A>(for result: Result<A,Error>) -> A?
    func resultValue<A>(for result: Result<A,FetchOutcome>) -> A?
}

internal extension ResultViewController where Self: UIViewController & AlertViewController & ErrorMessageViewController {
    
    func resultValue<A>(for result: Result<A,Error>) -> A?
    {
        switch result
        {
        case .failure(let error):
            DispatchQueue.main.async
            { [weak self] in
                self?.displayAlert(for: error, animated: true)
                self?.messageLabel.text = error.localizedDescription
            }
            return nil
        case .success(let value):
            return value
        }
    }
    
    func resultValue<A>(for result: Result<A,FetchOutcome>) -> A?
    {
        switch result
        {
        case .failure(let error):
            DispatchQueue.main.async
            { [weak self] in
                self?.displayAlert(for: error, animated: true)
                self?.messageLabel.text = error.localizedDescription
            }
            return nil
        case .success(let value):
            return value
        }
    }
    
}

internal extension ResultViewController where Self: UIViewController {
    
    func resultValue<A>(for result: Result<A,Error>) -> A?
    {
        switch result
        {
        case .failure:
            return nil
        case .success(let value):
            return value
        }
    }
    
    func resultValue<A>(for result: Result<A,FetchOutcome>) -> A?
    {
        switch result
        {
        case .failure:
            return nil
        case .success(let value):
            return value
        }
    }
    
}

