 //
//  PaintingDetailDataSource.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-13.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit
import PopUpLib
import KiteSDK
 
final class PaintingDetailDataSource: DataSource {
    
    private var relatedPaintingsDataSource: PaintingRowDataSource?
    
    func appendBuyMerch(_ painting: Painting, vcDelegate: PaintingDetailsViewController) -> IndexSet {
        guard painting.flags != 2 || painting.creationYear < 1920 else {
            return .init()
        }
        
        let buy = NSLocalizedString("Buy Merch", comment: "").uppercased()
        
        let config: (UIButton) -> () =
        { button in
            button.setTitle(buy, for: .normal)
            button.tintColor = AppStyle.Color.caranation
        }
        
        let action: (UIButton) -> () =
        { [weak vcDelegate] button in
            let imageURL = painting.image(size: .original)
            let kiteViewController = OLKiteViewController.init(assets: [OLAsset(url: URL(string: imageURL), size: .init(width: painting.width, height: painting.height))])
            kiteViewController?.modalPresentationStyle = .overFullScreen
            vcDelegate?.present(kiteViewController!, animated: true, completion: nil)
        }
        
        let data: DataType = [
            [(ButtonCellViewModel(config: config, pressed: action), ButtonTableViewCell.self)]
                ]
        
        return appendList(with: data)
    }
    
    func appendBuy(_ painting: Painting, vcDelegate: PaintingDetailsViewController & PopupStoreControllerDelegate) -> IndexSet {
        guard painting.flags != 2 || painting.creationYear < 1920 else {
            return .init()
        }
        
        let buy = NSLocalizedString("Buy", comment: "").uppercased()
        
        let config: (UIButton) -> () =
        { button in
            button.setTitle(buy, for: .normal)
            button.tintColor = AppStyle.Color.caranation
        }
        
        let action: (UIButton) -> () =
        { [weak vcDelegate] button in
            vcDelegate?.showLoading()
            Current.imageServie.load(from: painting.image(size: .original))
            { (_, _, image) in
                DispatchQueue.main.async {
                    vcDelegate?.hideLoading()
                    guard let image = image,
                        let popUp = PopupStoreController(image: image,
                                                     applicationKey: Secrets.CanvasPopKeys.accessKey,
                                                     delegate: vcDelegate)
                    else { return }
                    popUp.shouldDisplayStatusBar = true
                    popUp.modalPresentationStyle = .overFullScreen
                    vcDelegate?.present(popUp, animated: true, completion: nil)
                }
            }
        }
        
        let data: DataType = [
            [(ButtonCellViewModel(config: config, pressed: action), ButtonTableViewCell.self)]
                ]
        
        return appendList(with: data)
    }
    
    func appendFav(_ painting: Painting) -> IndexSet {

        let add = NSLocalizedString("Add to favorites", comment: "").uppercased()
        let remove = NSLocalizedString("Remove from favorites", comment: "").uppercased()
       
        
        let config: (UIButton) -> () =
        { button in
            let s = Current.favouritesService.contains(painting)
            button.setTitle(s ? remove : add, for: .normal)
            button.tintColor = AppStyle.Color.caranation
        }
        
        let action: (UIButton) -> () =
        { button in

            let fav = Current.favouritesService.contains(painting)
            if fav {
                Current.favouritesService.remove(painting: painting)
                button.setTitle(add, for: .normal)
            }
            else {
                Current.favouritesService.append(painting: painting)
                button.setTitle(remove, for: .normal)
            }
        }
        
        let data: DataType = [
            [(ButtonCellViewModel(config: config, pressed: action), ButtonTableViewCell.self)]
                ]
        return appendList(with: data)
    }
    
    func appendShare(_ painting: Painting, vc presenting: UIViewController) -> IndexSet {

        let Share = NSLocalizedString("Share", comment: "").uppercased()

        let config: (UIButton) -> () =
        { button in
            button.setTitle(Share, for: .normal)
            button.tintColor = AppStyle.Color.caranation
        }
        
        let action: (UIButton) -> () =
        { button in
            var share: [Any] = []
            if let url = URL(string: "https://" + Secrets.Api.Endpoint.production + painting.paintingURL) {
                share.append(url)
            }
            if let image = ImageViewModel(painting, paintingQulity: .original).imageUse {
                share.append(image)
            }
            else if let image = ImageViewModel(painting).imageUse {
                share.append(image)
            }
            
            let vc = UIActivityViewController(activityItems: share, applicationActivities:nil)
            vc.popoverPresentationController?.sourceView = button
            presenting.present(vc, animated: true, completion: nil)
        }
        
        let data: DataType = [
            [(ButtonCellViewModel(config: config, pressed: action), ButtonTableViewCell.self)]
                ]
        return appendList(with: data)
    }
    
    func appendPainting(_ painting: Painting) -> IndexSet
    {
        let data: DataType =
            [
                [(ImageViewModel(painting, paintingQulity: .original), ImageTableViewCell.self),
                 (TitleCellViewModel(painting), TitleTableViewCell.self)]
            ]
        return appendList(with: data)
    }
    
    func appendPaintingDetails(_ details: PaintingDetails) -> IndexSet
    {
        
        let displayDetails = PaintingDetailsDisplay(details: details)
        let rows:[(Any, ValueCell.Type)] = displayDetails.displayDict.map {
            (ColumnCellViewModel(NSLocalizedString($0.0, comment: "").uppercased(), $0.1, .row), (ColumnTableViewCell.self))
        }
        let data: DataType = [ rows ]
        return appendList(with: data)
    }
    
    func appendRelatedPaintings(_ realtedPaintings: [Painting], delegate: PaintingRowDataSourceDelegate?) -> IndexSet
    {
        let layout = Current.layoutService.paintingRowFlow()
        
        self.relatedPaintingsDataSource = PaintingRowDataSource(relatedPaintings: realtedPaintings, delegate: delegate)
        let collectionViewRow: [(Any, ValueCell.Type)] = [(CollectionViewCellModel(dataSouce: relatedPaintingsDataSource!, delegate: relatedPaintingsDataSource!, layout: layout), CollectionViewTableViewCell.self)]
        let title = [(LineCellViewModel(.init(NSLocalizedString("Related Paintings", comment: "").uppercased(), .lightHeading), .none, .clear) as Any, (LineTableViewCell.self as ValueCell.Type))]
        return appendList(with: [title + collectionViewRow])
    }
    
    override func configureCell(tableCell cell: UITableViewCell, withValue value: Any) {
        switch (cell, value) {
        case let (cell as LineTableViewCell, value as LineCellViewModel):
            cell.configureWith(value: value)
        case let (cell as ColumnTableViewCell, value as ColumnCellViewModel):
            cell.configureWith(value: value)
        case let (cell as ButtonTableViewCell, value as ButtonCellViewModel):
            cell.configureWith(value: value)
        case let (cell as ImageTableViewCell, value as ImageViewModel):
            cell.configureWith(value: value)
        case let (cell as TitleTableViewCell, value as TitleCellViewModel):
            cell.configureWith(value: value)
        case let (cell as CollectionViewTableViewCell, value as CollectionViewCellModel):
            cell.configureWith(value: value)
        default:
            assertionFailure("Unrecognized (cell, viewModel) combo.")
        }
    }
    
}
