package com.example.wikiart.api

import com.example.wikiart.model.ArtistCategory
import com.example.wikiart.model.ArtistList

class ArtistsRepository(
    private val language: String = getLanguage(),
) {
    suspend fun getArtists(
        category: ArtistCategory,
        page: Int,
        section: String? = null,
    ): ArtistList {
        return ApiClient.service.artistsByCategory(
            language = language,
            category = category.path,
            page = page,
            searchTerm = section,
        )
    }
}
