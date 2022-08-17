//
//  PaintingCollectionViewController.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-19.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

final class PaintingSectionViewController: SearchAutoCompleteCollectionViewController {
    
    private var fetcher: PaintingSectionListFectcher?
    private var dataSource = PaintingCollectionDataSource()
    private var segment: UISegmentedControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        collectionView!.setCollectionViewLayout(Current.layoutService.paintingSheet(dataSource: self), animated: true)
        dataSource.registerClasses(collectionView: collectionView)
        collectionView!.dataSource = dataSource
        
        segment = UISegmentedControl.paintingsLayoutsControl()
        segment.addTarget(self, action: #selector(segmentChanged(_:)), for: .valueChanged)
        navigationItem.rightBarButtonItem = UIBarButtonItem(customView: segment)
        
        fetcher = PaintingSectionListFectcher(auto: autoComplete)
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
        collectionView.scrollRectToVisible(cell.frame, animated: false)
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
        let begin =
        { [unowned self] in
            self.toggleLoadingView()
        }
        
        fetcher?.nextPage(willBeginFetch: begin)
        { [unowned self] result  in
            self.toggleLoadingView()
            guard let paintingResult = self.resultValue(for: result) else { return }
            
            if self.dataSource.rows(for: 0) == 0 {
                _ = self.dataSource.appendPaintings(paintingResult)
                self.collectionView.reloadData()
            }
            else {
                let indexPaths = self.dataSource.appendPaintings(paintingResult)
                self.collectionView.performBatchUpdates({
                    self.collectionView.insertItems(at: indexPaths)
                }, completion: nil)
            }
        }
    }
}

// MARK: ItemSizeLayoutDataSource
extension PaintingSectionViewController: ItemSizeLayoutDataSource {
    
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
extension PaintingSectionViewController: PageViewControllerDelegate {
    
    func pageViewControllerDidDisplayPage(at index: Int) {
        
    }

}
