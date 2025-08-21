package com.example.wikiart.api

import com.example.wikiart.model.Painting

class ArtistPaintingsRepository(
    private val service: WikiArtService = ApiClient.service,
) {
    suspend fun getPaintings(path: String, page: Int): List<Painting> {
        return service.artistPaintings("$path/all-works", page = page).Paintings
    }
}
