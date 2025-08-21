package com.example.wikiart.api

import com.example.wikiart.model.ArtistCategory
import com.example.wikiart.model.ArtistSection

/**
 * Repository providing sections for a given artist category.
 */
class ArtistsCategoryRepository(
    private val service: WikiArtService = ApiClient.service,
    private val language: String = getLanguage(),
) {
    suspend fun getSections(category: ArtistCategory): List<ArtistSection> {
        return service.artistSections(
            language = language,
            category = category.path,
        ).items
    }
}

