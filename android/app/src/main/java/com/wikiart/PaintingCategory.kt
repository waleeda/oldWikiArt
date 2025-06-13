package com.wikiart

enum class PaintingCategory : CategoryItem {
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

    override fun nameRes(): Int = when (this) {
        MEDIA -> R.string.painting_category_media
        STYLE -> R.string.painting_category_style
        GENRE -> R.string.painting_category_genre
        HIGH_RES -> R.string.painting_category_high_res
        POPULAR -> R.string.painting_category_popular
        FEATURED -> R.string.painting_category_featured
        FAVORITES -> R.string.painting_category_favorites
    }

   override fun iconRes(): Int = when (this) {
        MEDIA -> R.drawable.ic_painting_media
        STYLE -> R.drawable.ic_painting_style
        GENRE -> R.drawable.ic_painting_genre
        HIGH_RES -> R.drawable.ic_painting_high_res
        POPULAR -> R.drawable.ic_painting_popular
        FEATURED -> R.drawable.ic_painting_featured
        FAVORITES -> R.drawable.ic_favorite_filled
    }
}
