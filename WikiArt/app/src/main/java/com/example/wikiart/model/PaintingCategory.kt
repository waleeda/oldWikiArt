package com.example.wikiart.model

import com.example.wikiart.R

enum class PaintingCategory(val param: String, val titleRes: Int) {
    FEATURED("featured", R.string.category_featured),
    POPULAR("popular", R.string.category_popular),
    HIGH_RES("high_resolution", R.string.category_high_res),

    MEDIA("media", R.string.category_media),
    STYLE("style", R.string.category_style),
    GENRE("genre", R.string.category_genre),
    FAVORITES("favorites", R.string.category_favorites)
}
