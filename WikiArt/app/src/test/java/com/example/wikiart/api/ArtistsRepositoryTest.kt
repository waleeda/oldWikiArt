package com.example.wikiart.api

import com.example.wikiart.data.local.ArtistDao
import com.example.wikiart.data.local.CACHE_TIMEOUT
import com.example.wikiart.data.local.ArtistEntity
import com.example.wikiart.data.local.toEntity
import com.example.wikiart.model.Artist
import com.example.wikiart.model.ArtistCategory
import com.example.wikiart.model.ArtistList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ArtistsRepositoryTest {

    private class FakeArtistDao(initial: List<ArtistEntity>) : ArtistDao {
        var list: List<ArtistEntity> = initial
        var deleted = false
        override suspend fun getArtists(category: String, page: Int, section: String?): List<ArtistEntity> = list
        override suspend fun insertAll(artists: List<ArtistEntity>) { list = artists }
        override suspend fun delete(category: String, page: Int, section: String?) { deleted = true; list = emptyList() }
    }

    private class Service : FakeWikiArtService() {
        var calls = 0
        override suspend fun artistsByCategory(language: String, category: String, page: Int, json: Int, layout: String, searchTerm: String?): ArtistList {
            calls++
            val artist = Artist("net", "title", null, null, null, null, null)
            return ArtistList(listOf(artist), 1, 1)
        }
    }

    @Test
    fun staleArtistsAreRefreshed() = runBlocking {
        val now = System.currentTimeMillis()
        val stale = now - CACHE_TIMEOUT - 1
        val cachedArtist = Artist("id", "title", null, null, null, null, null)
        val dao = FakeArtistDao(listOf(cachedArtist.toEntity(ArtistCategory.POPULAR.path, 1, null, stale)))
        val service = Service()
        val repo = ArtistsRepository(artistDao = dao, service = service)

        val result = repo.getArtists(ArtistCategory.POPULAR, 1)

        assertEquals(1, service.calls)
        assertTrue(dao.deleted)
        assertEquals("net", result.Artists[0].id)
    }
}
