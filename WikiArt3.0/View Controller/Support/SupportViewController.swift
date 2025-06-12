//
//  SupportViewController.swift
//  WikiArt2.0
//
//  Created by Waleed Azhar on 2019-10-09.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit
import StoreKit
import MessageUI

public typealias ProductsRequestCompletionHandler = (_ success: Bool, _ products: [SKProduct]?) -> Void

class SupportViewController: UIViewController, MFMailComposeViewControllerDelegate {
    
    @IBOutlet weak var d: UIButton!
    private var purchasedProductIdentifiers: Set<String> = []
    private var productsRequest: SKProductsRequest?
    private var productsRequestCompletionHandler: ProductsRequestCompletionHandler?

    @IBOutlet weak var titleLab: UILabel!
    @IBOutlet weak var lable: UILabel!
    @IBOutlet weak var feedBack: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        SKPaymentQueue.default().add(self)
        
        navigationItem.leftBarButtonItem  = .init(barButtonSystemItem: .done, target: self, action: #selector(hide))
        d.setTitle(NSLocalizedString("Support", comment: ""), for: .normal)
        lable.text = NSLocalizedString("supportText", comment: "")
        feedBack.setTitle(NSLocalizedString("supportFeedBack", comment: ""), for: .normal)
        titleLab.text = NSLocalizedString("supportTitle", comment: "")
    }
    
    @objc func hide() {
        dismiss(animated: true, completion: nil )
    }
    
    @IBAction func sendFeedback(_ sender: Any) {
        sendEmail()
    }
    
    func sendEmail() {
        if MFMailComposeViewController.canSendMail() {
            let mail = MFMailComposeViewController()
            mail.mailComposeDelegate = self
            mail.setToRecipients(["wikiartfeedback@icloud.com"])
            mail.setMessageBody("<p>Dear WikiArt</p>", isHTML: true)

            present(mail, animated: true)
        } else {
            // show failure alert
        }
    }

    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        SKPaymentQueue.default().remove(self)
    }

    @IBAction func supportPressed(_ sender: Any) {
        let ids = ["wikiart.support4", "wiki.level3", "wikiart.level2", "wikiart.support1", "generous"]
        requestProducts(ids)
        { [unowned self] (loaded, products) in
            DispatchQueue.main.async {
                let alert = UIAlertController(title: NSLocalizedString("Support", comment: ""), message: nil, preferredStyle: .actionSheet)
                (products?.sorted { Unicode.CanonicalCombiningClass(rawValue: Unicode.CanonicalCombiningClass.RawValue(truncating: $0.price)) > Unicode.CanonicalCombiningClass(rawValue: Unicode.CanonicalCombiningClass.RawValue(truncating: $1.price)) })?.forEach({ (pro) in
                    alert.addAction(UIAlertAction.init(title: pro.localizedTitle + " \(pro.priceLocale.currencySymbol ?? "")\(pro.price.description)",
                                                       style: .default,
                                                       handler:
                        { (action) in
                            self.buyProduct(pro)
                    }))
                    
                })
                alert.addAction(.init(title: "Dismiss", style: .cancel, handler: { (_) in
                    self.presentedViewController?.dismiss(animated: true, completion: nil)
                }))
                alert.popoverPresentationController?.sourceView = self.d
                alert.popoverPresentationController?.canOverlapSourceViewRect = true
                self.present(alert, animated: true, completion: nil)
                
            }

        }
    }
    
    public func requestProducts(_ products:[String],
                                _ completionHandler: @escaping ProductsRequestCompletionHandler) {
           productsRequest?.cancel()
           productsRequestCompletionHandler = completionHandler
           productsRequest = SKProductsRequest(productIdentifiers: Set(products))
           productsRequest!.delegate = self
           productsRequest!.start()
       }
       
       public func buyProduct(_ product: SKProduct) {
           print("Buying \(product.productIdentifier)...")
           let payment = SKPayment(product: product)
           SKPaymentQueue.default().add(payment)
       }
    
    private func complete(transaction: SKPaymentTransaction) {
        SKPaymentQueue.default().finishTransaction(transaction)
    }
    
    private func restore(transaction: SKPaymentTransaction) {
        guard let _ = transaction.original?.payment.productIdentifier else { return }
        SKPaymentQueue.default().finishTransaction(transaction)
    }
    
    private func fail(transaction: SKPaymentTransaction) {
        print("fail...")
        if let transactionError = transaction.error as NSError?,
            let localizedDescription = transaction.error?.localizedDescription,
            transactionError.code != SKError.paymentCancelled.rawValue {
            print("Transaction Error: \(localizedDescription)")
        }
        
        SKPaymentQueue.default().finishTransaction(transaction)
    }

}

extension SupportViewController: SKProductsRequestDelegate {
    
    public func productsRequest(_ request: SKProductsRequest, didReceive response: SKProductsResponse) {
        let products = response.products
        productsRequestCompletionHandler?(true, products)
        clearRequestAndHandler()
    }
    
    public func request(_ request: SKRequest, didFailWithError error: Error) {
        print("Failed to load list of products.")
        print("Error: \(error.localizedDescription)")
        productsRequestCompletionHandler?(false, nil)
        clearRequestAndHandler()
    }
    
    private func clearRequestAndHandler() {
        productsRequest = nil
        productsRequestCompletionHandler = nil
    }
}


// MARK: - SKPaymentTransactionObserver

extension SupportViewController: SKPaymentTransactionObserver {
    
    public func paymentQueue(_ queue: SKPaymentQueue, updatedTransactions transactions: [SKPaymentTransaction]) {
        for transaction in transactions {
            switch (transaction.transactionState) {
            case .purchased:
                complete(transaction: transaction)
                break
            case .failed:
                fail(transaction: transaction)
                break
            case .restored:
                restore(transaction: transaction)
                break
            case .deferred:
                break
            case .purchasing:
                break
            @unknown default:
                return
            }
        }
    }
}
