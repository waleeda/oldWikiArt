package com.example.wikiart.model

import com.example.wikiart.R

/**
 * Categories for artist listings.
 */
enum class ArtistCategory(val param: String, val titleRes: Int) {
    /**
     * Popular artists served by the API.
     */
    POPULAR("popular", R.string.category_popular),
    /**
     * Favourite artists stored locally (not yet implemented).
     */
    FAVORITES("favorites", R.string.category_favorites)
}
