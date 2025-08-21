package com.example.wikiart.api

import com.example.wikiart.model.ArtistDetails
import com.example.wikiart.model.Painting

class ArtistDetailsRepository(
    private val service: WikiArtService = ApiClient.service
) {
    suspend fun getDetails(path: String): ArtistDetails {
        return service.artistDetails(path)
    }

    suspend fun getPaintings(path: String): List<Painting> {
        // WikiArt API uses "/mode/featured" to list well known paintings for an artist
        val list = service.artistPaintings("$path/mode/featured")
        return list.Paintings
    }
}
