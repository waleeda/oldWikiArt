//
//  PaintingDetailViewController.swift
//  Sticky Art
//
//  Created by Waleed Azhar on 2019-08-11.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit
import PopUpLib

final class PaintingDetailsViewController: TableViewController, AlertViewController, ResultViewController {
    
    private let content = PaintingDetailDataSource()

    private var detailsFetcher: PaintingDetailsFetcher?
    private var relatedPaintingsFetcher: RelatedPaintingsFetcher?
    
    private let painting: Painting
    private var details: PaintingDetails?
    private var related: [Painting]?
    
    init(_ painting: Painting) {
        self.painting = painting
        super.init(style: UITableView.Style.plain)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        _  = content.appendPainting(self.painting)
        _ = content.appendFav(self.painting)
        _ = content.appendBuy(painting, vcDelegate: self)
        _ = content.appendBuyMerch(painting, vcDelegate: self)
        _ = content.appendShare(painting, vc: self)
        
        tableView.dataSource = content
        tableView.separatorStyle = .none
        tableView.allowsMultipleSelection = false
        tableView.backgroundColor = .clear
        
        loadDetails()
    }
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        self.tableView.reloadData()
    }
    
    func loadDetails() {
        toggleLoadingView()
        detailsFetcher = PaintingDetailsFetcher(painting: self.painting)
        detailsFetcher?.get(willBeginFetch: nil)
        { [weak self] result  in
            self?.loadRelatedArt()
            self?.details = self?.resultValue(for: result)
        }
    }
    
    func loadRelatedArt() {
        relatedPaintingsFetcher = RelatedPaintingsFetcher(painting: painting)
        relatedPaintingsFetcher?.get(willBeginFetch: nil)
        { [weak self] result  in
            self?.toggleLoadingView()
            self?.related = self?.resultValue(for: result)
            self?.addContent()
        }
    }
    
    func addContent() {
        var i = 1
        if let details = details {
            _ = content.appendPaintingDetails(details)
            i += 1
        }
        if let realted = related {
            _ = content.appendRelatedPaintings(realted, delegate: self)
            i += 1
        }
        tableView.reloadData()
    }
}

extension PaintingDetailsViewController: PaintingRowDataSourceDelegate {
    
    func newHeight() {
        //tableView.reloadData()
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let (value, cell) = content.data[indexPath.section][indexPath.row]
        switch (cell, value) {
        case let (_ as ImageTableViewCell.Type, value as ImageViewModel):
            let aspectHeight = (tableView.frame.width / value.width) * value.height
            return aspectHeight
        case let (_ as CollectionViewTableViewCell.Type, value as CollectionViewCellModel):
            if let ds = value.dataSouce as? PaintingRowDataSource {
                return ds.cellHeight + 16
            }
            return 240
        default:
            return UITableView.automaticDimension
        }
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let (value, cellType) = content.data[indexPath.section][indexPath.row]
        switch (cellType, value) {
        case (_ as TitleTableViewCell.Type, _ as TitleCellViewModel):
            showArtist(for: painting)
        case let (_ as ImageTableViewCell.Type, value as ImageViewModel):
            tableView.scrollToTop(animated: true)
            let vm = ImageViewModel(painting.image(size: .original),
                                    value.placeholder,
                                    Double(value.width),
                                    Double(value.height))
            guard let cell = tableView.cellForRow(at: indexPath) else { return }
            showImageDetails(vm: vm, for: cell, superView: tableView)
        default:
            return
        }
    }
    
    func paintingRow(_ row :PaintingRowDataSource,_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard
            let cell = collectionView.cellForItem(at: indexPath),
            let paintings = row[section: 0] as? [Painting]
        else { return }
        collectionView.scrollRectToVisible(cell.frame, animated: true)
        showDetails(paintings, index: indexPath.row, for: cell, superView: collectionView, pageDelegate: self)
    }
}

extension PaintingDetailsViewController: PageViewControllerDelegate {
    
    func pageViewControllerDidDisplayPage(at index: Int) {
        
    }
}

extension PaintingDetailsViewController: Scrollable {
    
    func scrollToTop(animated: Bool) {
        tableView.scrollToTop(animated: animated)
    }
    
}

extension PaintingDetailsViewController: PopupStoreControllerDelegate {
    
    func popupStoreLibWasClosed(_ controller: PopupStoreController!) {
        controller.dismiss(animated: true, completion: nil)
    }
    
}
