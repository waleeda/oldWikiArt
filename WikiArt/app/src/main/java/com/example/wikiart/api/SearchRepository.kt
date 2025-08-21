package com.example.wikiart.api

import com.example.wikiart.model.ArtistList
import com.example.wikiart.model.AutocompleteResult
import com.example.wikiart.model.PaintingList

class SearchRepository(
    private val service: WikiArtService = ApiClient.service
) {
    suspend fun searchPaintings(term: String, page: Int): PaintingList {
        return service.searchPaintings(language = "en", term = term, page = page)
    }

    suspend fun searchArtists(term: String, page: Int): ArtistList {
        return service.searchArtists(language = "en", term = term, page = page)
    }

    suspend fun autocomplete(term: String): AutocompleteResult {
        return service.autocomplete(language = "en", term = term)
    }
}
