package com.example.wikiart.api

import com.example.wikiart.model.ArtistDetails
import com.example.wikiart.model.Painting

class ArtistDetailsRepository(
    private val service: WikiArtService = ApiClient.service
) {
    suspend fun getArtistDetails(id: String, language: String = "en"): ArtistDetails {
        return service.artistDetails(language, id)
    }

    suspend fun getArtistPaintings(id: String, language: String = "en"): List<Painting> {
        return service.paintingsByArtist(language, id).Paintings
    }
}
