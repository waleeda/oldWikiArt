package com.wikiart

enum class PaintingCategory {
    MEDIA,
    STYLE,
    GENRE,
    HIGH_RES,
    POPULAR,
    FEATURED,
    FAVORITES;

    fun hasSections(): Boolean = when (this) {
        MEDIA, STYLE, GENRE -> true
        else -> false
    }

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
