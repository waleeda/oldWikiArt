//
//  ArtistViewController.swift
//  WikiArt3.0
//
//  Created by Waleed Azhar on 2019-10-22.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit

final class ArtistsViewController: CollectionViewController, ResultViewController, AlertViewController {
    
    let categoriesDataSource = ArtistCategoryDataSource()
    private var dataSource = ArtistsDataSource()
    
    private var selectedCategory: ArtistCategory = .popular
    private var selectedSection: ArtistsSection?
    
    private var artistSectionsFetcher: ArtistsCategorySectionsFetcher?
    private var artistListFetcher: ArtistsListFectcher?
    
    convenience init() {
        self.init(collectionViewLayout: UICollectionViewFlowLayout())
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        dataSource.registerClasses(collectionView: collectionView)
        
        collectionView.dataSource = dataSource
        collectionView!.backgroundColor = .groupTableViewBackground
        
        navigationItem.title = selectedCategory.title
        
        artistListFetcher = ArtistsListFectcher(category: selectedCategory, section: selectedSection)
        
        let layout = Current.layoutService.artistColumnStack(dataSource: self)
        collectionView.setCollectionViewLayout(layout, animated: false)
        
        navigationItem.leftBarButtonItem = .init(title: NSLocalizedString("Categories", comment: ""), style: .plain, target: self, action: #selector(show(_:)))
        
        loadList()
    }

    @objc func show(_ sender: Any) {
         showCategories(delegate: self, dataSource: self)
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

extension ArtistsViewController: ItemSizeLayoutDataSource {
    
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

// MARK: FormControllerDelegate
extension ArtistsViewController: FormControllerDelegate, FormControllerDataSource {
    
    func formViewController(_ vc: FormController,
                            dataSourceFor index: Int,
                            loadNextForm: @escaping FormLoadCompletion) {
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
            selectedCategory = ArtistCategory.all[indexPath.row]
            if !selectedCategory.hasSections {
                artistListFetcher = .init(category: selectedCategory, section: nil)
                dataSource.removeAll()
                collectionView.reloadData()
                navigationItem.title = selectedCategory.title
                loadList()
            }
        }
        else if
            index == 1,
            let section = categoriesDataSource.data[indexPath.section][indexPath.row].model as? ArtistsSection {
            selectedSection = section
            artistListFetcher =  ArtistsListFectcher(category: selectedCategory, section: selectedSection)
            dataSource.removeAll()
            navigationItem.title = section.Title
            collectionView.reloadData()
            loadList()
        }
    }
    
    private func loadSection(_ loadNextForm: @escaping FormLoadCompletion) {
        artistSectionsFetcher = .init(category: selectedCategory)
        
        artistSectionsFetcher?.get(willBeginFetch: {})
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
