package com.example.wikiart.api

import com.example.wikiart.WikiArtApplication
import com.example.wikiart.data.local.CACHE_TIMEOUT
import com.example.wikiart.data.local.ArtistDao
import com.example.wikiart.data.local.toEntity
import com.example.wikiart.model.ArtistCategory
import com.example.wikiart.model.ArtistList

class ArtistsRepository(
    private val language: String = getLanguage(),
    private val artistDao: ArtistDao = WikiArtApplication.instance.database.artistDao(),
    private val service: WikiArtService = ApiClient.service,
) {
    suspend fun getArtists(
        category: ArtistCategory,
        page: Int,
        section: String? = null,
    ): ArtistList {
        val cached = artistDao.getArtists(category.path, page, section)
        val now = System.currentTimeMillis()
        if (cached.isNotEmpty()) {
            if (cached.all { now - it.updated < CACHE_TIMEOUT }) {
                val artists = cached.map { it.toModel() }
                return ArtistList(artists, artists.size, artists.size)
            } else {
                artistDao.delete(category.path, page, section)
            }
        }
        val result = service.artistsByCategory(
            language = language,
            category = category.path,
            page = page,
            searchTerm = section,
        )
        val entities = result.Artists.map { it.toEntity(category.path, page, section, now) }
        artistDao.insertAll(entities)
        return result
    }
}

