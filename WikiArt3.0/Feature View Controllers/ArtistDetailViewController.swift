//
//  Created by Waleed Azhar on 2019-08-11.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import Foundation
import UIKit

final class ArtistDetailsViewController : TableViewController, AlertViewController, ResultViewController {

    private let content = ArtistDetailDataSource()
    
    private var detailsFetcher: ArtistDetailsFetcher?
    private var famousPaintingsFetcher: FamousPaintingsFetcher?
    
    private let artist: Artist
    private var details: ArtistDetails?
    private var painting: [Painting]?

    init(_ artist: Artist) {
        self.artist = artist
        super.init(style: UITableView.Style.plain)
    }
    
    init(_ painting: Painting) {
        self.artist = Artist(id: nil,
                             title: painting.artistName,
                             year: nil,
                             nation: nil,
                             image: nil,
                             artistURL: painting.artistURL,
                             totalWorksTitle: nil)
        
        super.init(style: UITableView.Style.plain)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.dataSource = content
        tableView.separatorStyle = .none
        tableView.allowsMultipleSelection = false
        tableView.backgroundColor = .clear
        loadArtistDetails()
    }
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        self.tableView.reloadData()
    }
    
    func loadArtistDetails() {
        toggleLoadingView()
        detailsFetcher = ArtistDetailsFetcher(artist: artist)
        detailsFetcher?.get(willBeginFetch: nil)
        { [weak self] result  in
            self?.loadFamousPaintings()
            self?.details = self?.resultValue(for: result)
        }
    }
    
    func loadFamousPaintings() {
        famousPaintingsFetcher = FamousPaintingsFetcher(artist: artist)
        famousPaintingsFetcher?.get(willBeginFetch: nil)
        { [weak self] result  in
            self?.toggleLoadingView()
            self?.painting = self?.resultValue(for: result)
            self?.addContent()
        }
    }
    
    func addContent() {
        var i = 1
        if let details = details {
            let _ = content.appendArtistDetails(artist, details)
        } else {
            let _ = content.appendArtist(artist)
        }
        
        if let paintings = painting {
            let _ = content.appendFamousPaintings(paintings, delegate: self)
            i += 1
        }
        let _ = self.content.appendSeeAll()
        { [weak self] _ in
            self?.showAllPaintings(for: self!.artist)
            i += 1
        }
        
        tableView.reloadData()
    }
}

extension ArtistDetailsViewController: PaintingRowDataSourceDelegate, PageViewControllerDelegate {
    
    func pageViewControllerDidDisplayPage(at index: Int) {
        
    }
    
    
    func newHeight() {}
    
    func paintingRow(_ row :PaintingRowDataSource, _ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard
            let cell = collectionView.cellForItem(at: indexPath),
            let paintings = row[section: 0] as? [Painting]
        else { return }
        collectionView.scrollRectToVisible(cell.frame, animated: true)
        showDetails(paintings, index: indexPath.row, for: cell, superView: collectionView, pageDelegate: self)
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let (value, cell) = content.data[indexPath.section][indexPath.row]
        switch (cell, value) {
        case let (_ as CollectionViewTableViewCell.Type, value as CollectionViewCellModel):
            if let ds = value.dataSouce as? PaintingRowDataSource {
                return ds.cellHeight + 16
            }
            return 240
        default:
            return UITableView.automaticDimension
        }
    }
    
    override func tableView(_ tableView: UITableView, shouldHighlightRowAt indexPath: IndexPath) -> Bool {
        return false
    }
    
}

extension ArtistDetailsViewController: Scrollable {
    
    func scrollToTop(animated: Bool) {
        tableView.scrollToTop(animated: animated)
    }
    
}
