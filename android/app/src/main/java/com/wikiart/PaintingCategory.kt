package com.wikiart

enum class PaintingCategory {
    FAVORITES,
    FEATURED,
    POPULAR,
    MEDIA,
    STYLE,
    GENRE,
    HIGH_RES;

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
