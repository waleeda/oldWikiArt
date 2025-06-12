//
//  PaintingCollectionViewController.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-19.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit
import StoreKit

final class PaintingCollectionViewController: CollectionViewController, ResultViewController, AlertViewController, FavoritesDelegate {
    
    private let categoriesDataSource = SelectCategoryDataSource()
    
    private var dataSource = PaintingCollectionDataSource()
    
    private var fetcher: PaintingListFetcher
    
    private var sectionFetcher: CategorySectionsFetcher?
    
    private var selectedCategory: PaintingCategory
    
    private var segment: UISegmentedControl!
    
    init(category: PaintingCategory) {
        self.selectedCategory = category
        self.fetcher = .init(category: category)
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
        Current.favouritesService.delegate = self

        navigationItem.leftBarButtonItem = .init(title: NSLocalizedString("Categories", comment: ""), style: .plain, target: self, action: #selector(show(_:)))
        
        navigationItem.title = selectedCategory.title
        
        segment = UISegmentedControl.paintingsLayoutsControl()
        segment.addTarget(self, action: #selector(segmentChanged(_:)), for: .valueChanged)
        navigationItem.rightBarButtonItem = UIBarButtonItem(customView: segment)
    
        getNextPage()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        if (arc4random_uniform(15) == 1) {
            SKStoreReviewController.requestReview()
        }
    }
    
    func didChange() {
        if selectedCategory == .favorites {
            fetcher = .init(category: selectedCategory)
            dataSource.removeAll()
            getNextPage()
            
        }
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
       
    @objc func show(_ sender: Any) {
       showCategories(delegate: self, dataSource: self)
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
            fetcher.isFetching == false,
            !fetcher.isDepleted
        {
            getNextPage()
        }
    }

    private func getNextPage() {
        let begin =
        { [unowned self] in
            self.toggleLoadingView()
            self.segment.isEnabled = false
            
        }
        
        self.fetcher.nextPage(willBeginFetch: begin)
        { [unowned self] result  in
            defer {
                self.toggleLoadingView()
                self.segment.isEnabled = true
            }

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

extension PaintingCollectionViewController: PageViewControllerDelegate {
    
    func pageViewControllerDidDisplayPage(at index: Int) {
        
    }
    
}

// MARK: ItemSizeLayoutDataSource
extension PaintingCollectionViewController: ItemSizeLayoutDataSource {
    
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

// MARK: FormControllerDelegate
extension PaintingCollectionViewController: FormControllerDelegate, FormControllerDataSource {
    
    func formViewController(_ vc: FormController, dataSourceFor index: Int, loadNextForm: @escaping FormLoadCompletion) {
        switch index {
        case 0:
            _ = categoriesDataSource.setCategories()
            loadNextForm(categoriesDataSource)
        case 1:
           loadSection(loadNextForm)
        default:
            loadNextForm(nil)
        }
    }
    
    func formViewController(_ vc: FormController, formAt index: Int, didSelectRowAt indexPath: IndexPath) {
        if index == 0 {
            selectedCategory = PaintingCategory.all[indexPath.row]
            switch selectedCategory {
            case .featured, .highRes, .popular, .favorites:
                fetcher = .init(category: selectedCategory)
                navigationItem.title = selectedCategory.title
                dataSource.removeAll()
                collectionView.reloadData()
                getNextPage()
            default:
                return
            }
        }
        else if
            index == 1,
            let section = categoriesDataSource.data[indexPath.section][indexPath.row].model as? PaintingSection {
            fetcher = .init(category: .genre(id: section._id._oid))
            navigationItem.title = section.title
            dataSource.removeAll()
            collectionView.reloadData()
            getNextPage()
        }
    }
    
    private func loadSection(_ loadNextForm: @escaping FormLoadCompletion) {
        sectionFetcher = CategorySectionsFetcher(category: selectedCategory)
        
        sectionFetcher?.get(willBeginFetch: {})
        {  [weak self] result in
            guard
                let sections = self?.resultValue(for: result),
                let self = self
            else {
                loadNextForm(nil)
                return
            }
            _ = self.categoriesDataSource.set(category: self.selectedCategory, sections: sections)
            loadNextForm(self.categoriesDataSource)
        }
    }
}
