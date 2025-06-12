//
//  Created by Waleed Azhar on 2019-08-01.
//  Copyright © 2019 Waleed Azhar. All rights reserved.
//

import UIKit
import MessageUI

protocol ViewControllerRouter  {
    var transition: Transition? { get set }
    func showDetails(_ paintings: [Painting], index: Int, for cell: UICollectionViewCell, superView: UIView, pageDelegate: PageViewControllerDelegate?)
    func showDetails(_ artists: [Artist], index: Int, for cell: UICollectionViewCell, superView: UIView)
    func showArtist(for painting: Painting)
    func showCategories(delegate: FormControllerDelegate, dataSource: FormControllerDataSource)
    func showAllPaintings(for artist: Artist)
    func showAutoCompleteSection(auto: SearchAutoComplete)
    func showImageDetails(vm: ImageViewModel, for cell: UIView, superView: UIView)
}

extension ViewControllerRouter where Self : UIViewController {
    
    func showDetails(_ paintings: [Painting], index: Int, for cell: UICollectionViewCell, superView: UIView, pageDelegate: PageViewControllerDelegate?) {
        let vc = PageViewController<PaintingDetailsViewController>(paintings: paintings, index: index)
        vc.pageDelegate = pageDelegate
        let frame = (UIApplication.shared.keyWindow?.bounds ?? UIScreen.main.bounds).insetBy(dx: 0.0, dy: 48).applying(.init(translationX: 0, y: 48))
        let cardConfig = CardPresentationConfiguration(rect: frame,
                                                      sourceView: cell,
                                                      superView: superView)
        let currTransition: Transition = .init(animatorStyle: .pop(.init(sourceView: cell, superView: superView)),
                                               kind: .modal(style: .custom, overParent: true),
                                               presentaion: .card(cardConfig))
        make(transition: currTransition, to: vc)
    }
    
    func showDetails(_ artists: [Artist], index: Int, for cell: UICollectionViewCell, superView: UIView) {
        let vc = PageViewController<ArtistDetailsViewController>(artists: artists, index: index)
        
        let frame = (UIApplication.shared.keyWindow?.bounds ?? UIScreen.main.bounds).insetBy(dx: 0, dy: 48).applying(.init(translationX: 0, y: 48))
        let cardConfig = CardPresentationConfiguration(rect: frame,
                                                      sourceView: cell,
                                                      superView: superView)
        let currTransition: Transition = .init(animatorStyle: .slideUp,
                                               kind: .modal(style: .custom, overParent: true),
                                               presentaion: .card(cardConfig))
        make(transition: currTransition, to: vc)
    }
    
    func showCategories(delegate: FormControllerDelegate, dataSource: FormControllerDataSource) {
        let form = FormController()
        form.formDelegate = delegate
        form.formDataSource = dataSource

        form.modalPresentationStyle = .popover
        form.popoverPresentationController?.barButtonItem = navigationItem.leftBarButtonItem
        present(form, animated: true, completion: nil)
    }
    
    func showAllPaintings(for artist: Artist) {
        let vc = AllPaintingCollectionViewController(artist: artist)
        let nav = NavigationController(rootViewController: vc)
        nav.navigationBar.prefersLargeTitles = false
        nav.modalPresentationStyle = .overFullScreen
        present(nav, animated: true, completion: nil)
    }
    
    func showArtist(for painting: Painting) {
        let vc = ArtistDetailsViewController(painting)
        let frame = (UIApplication.shared.keyWindow?.bounds ?? UIScreen.main.bounds).insetBy(dx: 0, dy: 48).applying(.init(translationX: 0, y: 48))
        let cardConfig = CardPresentationConfiguration(rect: frame,
                                                      sourceView: nil,
                                                      superView: nil)
        let currTransition: Transition = .init(animatorStyle: .slideUp,
                                               kind: .modal(style: .custom, overParent: true),
                                               presentaion: .card(cardConfig))
        make(transition: currTransition, to: vc)
    }
    
    func showAutoCompleteSection(auto: SearchAutoComplete) {
        guard let type = auto.type else {
            return
        }
        
        switch type {
        case .paintingsCategory:
            let autoVC = PaintingSectionViewController(autoComplete: auto)
            navigationController?.pushViewController(autoVC, animated: true)
        case .artistCategory:
            let autoVC = ArtistsSectionViewController(autoComplete: auto)
            navigationController?.pushViewController(autoVC, animated: true)
        default:
            return
        }

    }
    
    func showImageDetails(vm: ImageViewModel, for cell: UIView, superView: UIView) {
        let vc = ImageDetailViewController(imageModel: vm)
        vc.modalPresentationStyle = .overFullScreen
        let transition = Transition(animatorStyle: .zoom(.init(sourceView: cell, superView: superView)),
                                    kind:.modal(style: .custom, overParent: true),
                                    presentaion: .standard)
    
        make(transition: transition, to: vc)
    }
    
    fileprivate func make(transition: Transition?, to vc: UIViewController) {
        guard let transition = transition else {
            self.show(vc, sender: self)
            return
        }

        vc.transitioningDelegate = transition
        
        switch transition.kind {
        case .detail:
            self.show(vc, sender: self)
            var presenting = (vc.navigationController ?? vc.tabBarController) as? ViewControllerRouter
            presenting?.transition = transition
        case let .modal(style, parentDefinesContext):
            self.definesPresentationContext = parentDefinesContext
            vc.modalPresentationStyle = style
            self.present(vc,animated: transition.animated)
            {
                var presenting = vc.presentingViewController as? ViewControllerRouter
                presenting?.transition = transition
            }
        }
    }
}



