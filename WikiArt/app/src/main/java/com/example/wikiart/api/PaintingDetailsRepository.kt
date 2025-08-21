package com.example.wikiart.api

import com.example.wikiart.model.Painting

class PaintingDetailsRepository(
    private val service: WikiArtService = ApiClient.service,
    private val language: String = getLanguage(),
) {
    suspend fun getPainting(id: String): Painting {
        return service.paintingDetails(language, id)
    }
}
