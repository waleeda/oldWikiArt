package com.wikiart

enum class PaintingCategory {
    MEDIA,
    STYLE,
    GENRE,
    HIGH_RES,
    POPULAR,
    FEATURED,
    FAVORITES;

    override fun toString(): String = when (this) {
        MEDIA -> "Media"
        STYLE -> "Style"
        GENRE -> "Genre"
        HIGH_RES -> "High Res"
        POPULAR -> "Popular"
        FEATURED -> "Featured"
        FAVORITES -> "Favorites"
    }
}
