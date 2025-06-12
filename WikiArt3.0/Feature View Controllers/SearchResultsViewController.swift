//
//  Created by Waleed Azhar on 2019-10-25.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

final class SearchResultsViewController: TableViewController, UISearchBarDelegate, ResultViewController, UISearchResultsUpdating, AlertViewController {

    private lazy var searchController = UISearchController(searchResultsController: nil)
    
    private var artists:[Artist] = []
    private var paintings:[Painting] = []
    
    private var artistsModel: CollectionViewCellModel
    private var paintingsModel: CollectionViewCellModel
    
    private var auto: [SearchAutoComplete] = []
    
    private var fetcher: AutoCompleteSearchResultsFetcher?
    private var artistsFetcher: ArtistSearchResultsFetcher?
    private var paintingsFetcher: PaintingSearchResultsFetcher?
   
    private var newTerm: String? = nil
    private var currTerm: String? = nil
    private var resultTerm: String? = nil
    
    init() {
        let row = PaintingRowDataSource(relatedPaintings: [], delegate: nil, term: nil)
        paintingsModel = CollectionViewCellModel(dataSouce: row, delegate: row, layout: Current.layoutService.paintingRowFlow())
        
        let row2 = ArtistRowDataSource(artists: [], delegate: nil, term: nil)
        artistsModel = CollectionViewCellModel(dataSouce: row2, delegate: row2, layout: Current.layoutService.paintingRowFlow())
        
        super.init(style: .plain)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.backgroundColor = .groupTableViewBackground
        title = NSLocalizedString("Search", comment: "")
        
        tableView.register(cell: CollectionViewTableViewCell.self)
        tableView.register(cell: CartTableViewCell.self)
        
        tableView.separatorStyle = .none
        navigationItem.hidesSearchBarWhenScrolling = true
        definesPresentationContext = false
    
        searchController.searchResultsUpdater = self
        searchController.obscuresBackgroundDuringPresentation = false
        searchController.searchBar.placeholder = NSLocalizedString("Enter term to start search", comment: "")
        navigationItem.searchController = searchController
        searchController.searchBar.delegate = self
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        3
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0 {
            return paintings.isEmpty ? 0 : 1
        }
        else if section == 1 {
            return artists.isEmpty ? 0 : 1
        }
        else {
            return auto.count
        }
    }
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if section == 0 {
            return "\(paintings.count) " + NSLocalizedString("Paintings", comment: "")
        }
        else if section == 1 {
            return "\(artists.count) " + NSLocalizedString("Artists", comment: "")
        }
        else {
            return "\(auto.count) " + NSLocalizedString("Sections", comment: "")
        }
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if indexPath.section == 0 {
            let cell:CollectionViewTableViewCell = tableView.dequeue(indexPath: indexPath)
            cell.configureWith(value: paintingsModel)
            return cell
        }
        else if indexPath.section == 1 {
            let cell:CollectionViewTableViewCell = tableView.dequeue(indexPath: indexPath)
            cell.configureWith(value: artistsModel)
            return cell
        }
        else {
            let cell: CartTableViewCell = tableView.dequeue(indexPath: indexPath)
            cell.configureWith(value: auto[indexPath.row], term: resultTerm ?? "")
            cell.accessoryType = .disclosureIndicator
            return cell
        }
    }
    
    func updateSearchResults(for searchController: UISearchController) {
        let text = searchController.searchBar.text
        if text != nil, text != "" {
            newTerm = text
        }
        else {
           clear()
        }
    }
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        if searchBar.text == "" {
            clear()
        }
    }

    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchController.dismiss(animated: true, completion: nil)
        fetch()
    }
    
    func loadArtists(term: String) {
        artistsFetcher = .init(term: term)
        artistsFetcher?.get(willBeginFetch: {})
        { [weak self] result in
            self?.loadPaintings(term: term)
            guard let artists = self?.resultValue(for: result) else { return }
            self?.artists = artists
            let row = ArtistRowDataSource(artists: artists, delegate: self, term: self?.resultTerm)
            self?.artistsModel = CollectionViewCellModel(dataSouce: row, delegate: row, layout: Current.layoutService.paintingRowFlow())
        }
    }
    
    func loadPaintings(term: String) {
        paintingsFetcher = .init(term: term)
        paintingsFetcher?.get(willBeginFetch: {})
        { [weak self] result in
            self?.toggleLoadingView()
            guard let paintings = self?.resultValue(for: result) else { return }
            self?.paintings = paintings
            let row = PaintingRowDataSource(relatedPaintings: paintings, delegate: self, term: self?.resultTerm)
            self?.paintingsModel = CollectionViewCellModel(dataSouce: row, delegate: row, layout: Current.layoutService.paintingRowFlow())
            
            if self?.newTerm == self?.resultTerm {
                self?.tableView.reloadData()
            }
            else {
                self?.clear()
            }
        }
    }
    
    func fetch() {
        toggleLoadingView()
        guard newTerm != currTerm, newTerm != resultTerm  else { return }
        let seachTerm = newTerm ?? ""
        fetcher = AutoCompleteSearchResultsFetcher(term: seachTerm)
        currTerm = seachTerm
        
        fetcher?.get(willBeginFetch: {})
        { [weak self] result in
            self?.loadArtists(term: seachTerm)
            guard let autos = self?.resultValue(for: result) else { return }
            self?.auto = autos
            self?.resultTerm = seachTerm
        }
    }
    
    fileprivate func clear() {
        //resultTerm = nil
        newTerm = nil
        currTerm = nil
        auto = []
        artists = []
        paintings = []
        fetcher = nil
        artistsFetcher = nil
        paintingsFetcher = nil
        hideLoading()
        tableView.reloadData()
    }

}

extension SearchResultsViewController {
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let content = indexPath.section == 0 ? paintingsModel.dataSouce : artistsModel.dataSouce
        if indexPath.section == 2 {
            return 64
        }
        else if indexPath.section == 0, let ds = content as? PaintingRowDataSource {
            return ds.cellHeight + 16
        }
        else if indexPath.section == 1, let ds = content as? ArtistRowDataSource {
            return ds.cellHeight + 16
        }
        
        return UITableView.automaticDimension
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        guard indexPath.section == 2 else { return }
        showAutoCompleteSection(auto: auto[indexPath.row])
    }
}

extension SearchResultsViewController: PaintingRowDataSourceDelegate, PageViewControllerDelegate {
    
    func pageViewControllerDidDisplayPage(at index: Int) {
        
    }
    
    func newHeight() {
        tableView.reloadData()
    }
    
    func paintingRow(_ row: PaintingRowDataSource, _ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard let cell = collectionView.cellForItem(at: indexPath) else { return }
        showDetails(paintings, index: indexPath.row, for: cell, superView: collectionView, pageDelegate: self)
    }
}


extension SearchResultsViewController: ArtistRowDataSourceDelegate {
    
    func newHeightForArtistRow() {
        tableView.reloadData()
    }
    
    func artistRow(_ row: ArtistRowDataSource, _ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard let cell = collectionView.cellForItem(at: indexPath) else { return }
        showDetails(artists, index: indexPath.row, for: cell, superView: collectionView)
    }
}

