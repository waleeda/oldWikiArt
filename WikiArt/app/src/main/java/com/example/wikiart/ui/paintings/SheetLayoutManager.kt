package com.example.wikiart.ui.paintings

import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Layout manager used for [PaintingAdapter.Layout.SHEET].
 *
 * It is essentially a single column vertical [StaggeredGridLayoutManager]
 * with gap handling disabled to mimic a full width list layout while still
 * letting each item measure its own height.
 */
class SheetLayoutManager : StaggeredGridLayoutManager(1, VERTICAL) {
    init {
        gapStrategy = GAP_HANDLING_NONE
    }
}
