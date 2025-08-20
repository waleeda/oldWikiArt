package com.example.wikiart.api

import com.example.wikiart.model.Painting

class PaintingDetailsRepository(
    private val service: WikiArtService = ApiClient.service
) {
    suspend fun getPainting(id: String, language: String = "en"): Painting {
        return service.paintingDetails(language, id)
    }
}
