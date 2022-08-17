//
//  ArtistViewController.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-22.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

final class ArtistsSectionViewController: SearchAutoCompleteCollectionViewController {
    
    private var artistListFetcher: ArtistSectionListFectcher?
    private var dataSource = ArtistsDataSource()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        dataSource.registerClasses(collectionView: collectionView)
        
        collectionView.dataSource = dataSource
        collectionView!.backgroundColor = .groupTableViewBackground
        artistListFetcher = ArtistSectionListFectcher(auto: autoComplete)
        
        let layout = Current.layoutService.artistColumnStack(dataSource: self)
        collectionView.setCollectionViewLayout(layout, animated: false)
        loadList()
    }
    
    func loadList() {
        let begin =
        { [unowned self] in
            self.toggleLoadingView()
        }
        artistListFetcher?.nextPage(willBeginFetch: begin)
        { [weak self] result  in
            self?.toggleLoadingView()
            guard let artists = self?.resultValue(for: result) else {
                return
            }
            let empty = self?.dataSource.data.isEmpty ?? true
            let indexPaths = self?.dataSource.append(artists: artists) ?? []
            
            self?.collectionView.performBatchUpdates({
                if empty {
                    self?.collectionView.insertSections(.init(integer: 0))
                }
                else {
                    self?.collectionView.insertItems(at:indexPaths)
                }
            }, completion: nil)
        }
    }
    
    override func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard
            let artists = dataSource[section: 0] as? [Artist],
            let cell = collectionView.cellForItem(at: indexPath)
        else { return }
        
        collectionView.scrollRectToVisible(cell.frame, animated: true)
        showDetails(artists, index: indexPath.row, for: cell, superView: collectionView)
    }
    
    override func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if
            indexPath == IndexPath(row: dataSource.data[0].count - 1, section: 0),
            indexPath != IndexPath(row: 0, section: 0),
            artistListFetcher?.isFetching == false,
            artistListFetcher?.hasRemainingPages == true
        {
            loadList()
        }
    }
}

extension ArtistsSectionViewController: ItemSizeLayoutDataSource {
    
    func numberOfItemsInLayout() -> Int {
        if dataSource.data.isEmpty {
            return 0
        }
        return dataSource.data[0].count
    }
    
    func layout(dimensionsForItemAt indexPath: IndexPath) -> CGSize {
        let width:CGFloat = 200
        return .init(width: width, height: width*1.3)
    }
    
    func bottomPaddingForItem(at indexPath: IndexPath, with itemSize: CGSize) -> CGFloat? {
        nil
    }
    
}
