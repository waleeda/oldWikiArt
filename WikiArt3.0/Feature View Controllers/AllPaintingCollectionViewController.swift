//
//  PaintingCollectionViewController.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-19.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

final class AllPaintingCollectionViewController: CollectionViewController, ResultViewController, AlertViewController {
    
    private var artist: Artist
    private var dataSource = PaintingCollectionDataSource()
    private var fetcher: ArtistPaintingListFectcher?
    private var sort: ArtistPaintingListSort = .date
    private var layoutSegment: UISegmentedControl!
    private var sortSegment: UISegmentedControl!
    
    init(artist: Artist) {
        self.artist = artist
        self.fetcher = .init(artist: artist, sort: sort)
        super.init(collectionViewLayout: UICollectionViewFlowLayout())
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    collectionView!.setCollectionViewLayout(Current.layoutService.paintingSheet(dataSource: self), animated: true)
        
        collectionView!.backgroundColor = .groupTableViewBackground
        dataSource.registerClasses(collectionView: collectionView)
        collectionView!.dataSource = dataSource
        
        layoutSegment = UISegmentedControl.paintingsLayoutsControl()
        layoutSegment.addTarget(self, action: #selector(segmentChanged(_:)), for: .valueChanged)
        
        sortSegment = UISegmentedControl.artistsSortControl()
        sortSegment.addTarget(self, action: #selector(sortSegmentChanged), for: .valueChanged)
        
        navigationItem.titleView = sortSegment
        navigationItem.rightBarButtonItem = UIBarButtonItem(customView: layoutSegment)
        
        navigationItem.leftBarButtonItem = .init(title: "↓", style: .plain, target: self, action: #selector(closePressed))
        navigationItem.leftBarButtonItem?.tintColor = .darkGray
        getNextPage()
    }
    
    @objc func closePressed() {
        dismiss(animated: true, completion: nil)
    }
    
    @objc func sortSegmentChanged(_ segment: UISegmentedControl) {
        guard let selectedSort = ArtistPaintingListSort(rawValue: segment.selectedSegmentIndex) else { return }
        sort = selectedSort
        fetcher = .init(artist: artist, sort: sort)
        dataSource.removeAll()
        
        collectionView.reloadData()
        getNextPage()
    }
    
    @objc func segmentChanged(_ segment: UISegmentedControl) {
        guard
            !collectionView.hasUncommittedUpdates,
            let layout = Layout(rawValue: segment.selectedSegmentIndex)
        else {
            return
        }
        dataSource.change(to: layout)
        collectionView.setCollectionViewLayout(Current.layoutService.collectionViewLayout(for: layout, dataSource: self), animated: true) { (_) in
            self.collectionView.reloadItems(at: self.collectionView.indexPathsForVisibleItems)
        }
    }
       
    // MARK: UICollectionViewDelegate

    override func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard
            let cell = collectionView.cellForItem(at: indexPath),
            let paintings = dataSource[section: 0] as? [Painting]
        else { return }
        collectionView.scrollRectToVisible(cell.frame, animated: true)
        showDetails(paintings, index: indexPath.row, for: cell, superView: collectionView, pageDelegate: self)
    }
    
    
    override func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if
            indexPath == IndexPath(row: dataSource.rows(for: 0) - 1, section: 0),
            indexPath != IndexPath(row: 0, section: 0),
            fetcher?.isFetching == false,
            fetcher?.isDepleted == false
        {
            getNextPage()
        }
    }

    private func getNextPage() {
        let begin = { [unowned self] in self.toggleLoadingView() }
        
        self.fetcher?.nextPage(willBeginFetch: begin)
        { [unowned self] result  in
            self.toggleLoadingView()
            
            guard let paintingResult = self.resultValue(for: result) else { return }
           
            guard self.dataSource.rows(for: 0) == 0 else {
                let indexPaths = self.dataSource.appendPaintings(paintingResult)
                self.collectionView.performBatchUpdates({
                    self.collectionView.insertItems(at: indexPaths)
                }, completion: nil)
                
                return
            }
           
           self.collectionView.performBatchUpdates({
                _ = self.dataSource.appendPaintings(paintingResult)
                self.collectionView.insertSections(.init(arrayLiteral: 0))
            }, completion: nil)
            
        }
    }
}

// MARK: ItemSizeLayoutDataSource
extension AllPaintingCollectionViewController: ItemSizeLayoutDataSource {
    
    func numberOfItemsInLayout() -> Int {
        dataSource.rows(for: 0)
    }
    
    func layout(dimensionsForItemAt indexPath: IndexPath) -> CGSize {
        let painting = dataSource[indexPath] as! Painting
        return CGSize(width: painting.width, height: painting.height)
    }
    
    func bottomPaddingForItem(at indexPath: IndexPath, with itemSize: CGSize) -> CGFloat? {
        let painting = dataSource[indexPath] as! Painting
        return Current.paddingService.bottomPaddingForItem(with: itemSize, with: painting)
    }
    
}

// MARK: PageDelegate
extension AllPaintingCollectionViewController: PageViewControllerDelegate {
    
    func pageViewControllerDidDisplayPage(at index: Int) {
        
    }

}
