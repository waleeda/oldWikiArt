package com.example.wikiart.ui.artists

import com.example.wikiart.api.ApiClient
import com.example.wikiart.model.Artist
import com.example.wikiart.model.ArtistCategory

/**
 * Repository responsible for retrieving artists from the API.
 */
class ArtistsRepository {
    /**
     * Load a page of [Artist]s for the given [category].
     * Favourites are not yet implemented so this returns an empty list for that category.
     */
    suspend fun loadArtists(page: Int, category: ArtistCategory): List<Artist> {
        return if (category == ArtistCategory.FAVORITES) {
            emptyList()
        } else {
            ApiClient.service.artists("en", page).Artists
        }
    }
}
