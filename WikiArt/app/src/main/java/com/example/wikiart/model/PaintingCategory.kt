package com.example.wikiart.model

import com.example.wikiart.R

enum class PaintingCategory(val param: String, val titleRes: Int) {
    FEATURED("featured", R.string.category_featured),
    POPULAR("popular", R.string.category_popular),
    HIGH_RES("high_resolution", R.string.category_high_res)
}
