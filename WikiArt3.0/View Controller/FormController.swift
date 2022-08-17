//
//  FormController.swift
//  test2
//
//  Created by Waleed Azhar on 2019-10-22.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

typealias FormLoadCompletion = (DataSource?) -> Void

protocol FormControllerDelegate: class {
    func formViewController(_ vc: FormController, formAt index: Int, didSelectRowAt indexPath: IndexPath)
}

protocol FormControllerDataSource: class {
    func formViewController(_ vc: FormController, dataSourceFor index: Int, loadNextForm: @escaping FormLoadCompletion)
}

final class FormController: UINavigationController, UINavigationControllerDelegate {
    
    weak var formDelegate: FormControllerDelegate?
    weak var formDataSource: FormControllerDataSource?
    private var index = -1
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationBar.tintColor = .darkGray
        view.backgroundColor = .white
        delegate = self
        
        addFormViewController()
        addCloseButton()
    }
    
    @objc func closePressed() {
        dismiss(animated: true, completion: nil)
    }
    
    func addCloseButton() {
        guard let vc = viewControllers.first else {
            return
        }
        
        vc.navigationItem.leftBarButtonItem = .init(title: "↓", style: .plain, target: self, action: #selector(closePressed))
    }

    private func addFormViewController() {
        let vc = TableViewController(style: .plain)
        vc.tableView.delegate = self
        pushViewController(vc, animated: true)
        vc.toggleLoadingView()
        let completion: FormLoadCompletion =
        { [weak self, weak vc] dataSource in
            defer {
                vc?.toggleLoadingView()
                self?.view.isUserInteractionEnabled = true
            }
            
            guard dataSource != nil else {
                self?.dismiss(animated: true, completion: nil)
                return
            }
            vc?.tableView.dataSource = dataSource
            self?.index += 1
        }
        
        view.isUserInteractionEnabled = false
        formDataSource?.formViewController(self, dataSourceFor: index + 1, loadNextForm: completion)
    }

   func navigationController(_ navigationController: UINavigationController,
                             didShow viewController: UIViewController,
                             animated: Bool) {
        if
            let newIndex = navigationController.viewControllers.firstIndex(of: viewController),
            newIndex < self.index
        {
            self.index  = newIndex
        }
    }
    
}

extension FormController: UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        formDelegate?.formViewController(self, formAt: index, didSelectRowAt: indexPath)
        addFormViewController()
    }
    
}
