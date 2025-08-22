package com.example.wikiart.api

import com.example.wikiart.WikiArtApplication
import com.example.wikiart.data.local.CACHE_TIMEOUT
import com.example.wikiart.data.local.SearchResultDao
import com.example.wikiart.data.local.toEntity
import com.example.wikiart.model.ArtistList
import com.example.wikiart.model.AutocompleteResult
import com.example.wikiart.model.PaintingList

class SearchRepository(
    private val service: WikiArtService = ApiClient.service,
    private val language: String = getLanguage(),
    private val searchDao: SearchResultDao = WikiArtApplication.instance.database.searchResultDao(),
) {
    suspend fun searchPaintings(term: String, page: Int): PaintingList {
        return service.searchPaintings(language = language, term = term, page = page)
    }

    suspend fun searchArtists(term: String, page: Int): ArtistList {
        return service.searchArtists(language = language, term = term, page = page)
    }

    suspend fun autocomplete(term: String): AutocompleteResult {
        val cached = searchDao.get(term)
        val now = System.currentTimeMillis()
        if (cached != null) {
            if (now - cached.updated < CACHE_TIMEOUT) {
                return cached.toModel()
            } else {
                searchDao.delete(term)
            }
        }
        val result = service.autocomplete(language = language, term = term)
        searchDao.insert(result.toEntity(term, now))
        return result
    }
}

