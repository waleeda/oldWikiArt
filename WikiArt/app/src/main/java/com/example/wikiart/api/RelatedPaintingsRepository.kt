package com.example.wikiart.api

import com.example.wikiart.model.Painting

class RelatedPaintingsRepository(
    private val service: WikiArtService = ApiClient.service
) {
    suspend fun getRelated(path: String): List<Painting> {
        return service.relatedPaintings(path).Paintings
    }
}
