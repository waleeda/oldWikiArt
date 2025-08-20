package com.example.wikiart.api

import com.example.wikiart.model.ArtistCategory
import com.example.wikiart.model.ArtistList

class ArtistsRepository {
    suspend fun getArtists(category: ArtistCategory, page: Int): ArtistList {
        return ApiClient.service.artistsByCategory(
            language = "en",
            category = category.path,
            page = page
        )
    }
}
