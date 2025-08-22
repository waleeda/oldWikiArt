package com.example.wikiart.api

import com.example.wikiart.data.local.CACHE_TIMEOUT
import com.example.wikiart.data.local.SearchResultDao
import com.example.wikiart.data.local.SearchResultEntity
import com.example.wikiart.data.local.toEntity
import com.example.wikiart.model.AutocompleteResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchRepositoryTest {

    private class FakeSearchDao(initial: SearchResultEntity?) : SearchResultDao {
        var entity: SearchResultEntity? = initial
        var deleted = false
        override suspend fun get(query: String): SearchResultEntity? = entity
        override suspend fun insert(result: SearchResultEntity) { entity = result }
        override suspend fun delete(query: String) { deleted = true; entity = null }
    }

    private class Service : FakeWikiArtService() {
        var calls = 0
        override suspend fun autocomplete(language: String, term: String, json: Int): AutocompleteResult {
            calls++
            return AutocompleteResult(listOf("net"))
        }
    }

    @Test
    fun staleAutocompleteIsRefreshed() = runBlocking {
        val now = System.currentTimeMillis()
        val stale = now - CACHE_TIMEOUT - 1
        val cached = AutocompleteResult(listOf("old"))
        val dao = FakeSearchDao(cached.toEntity("term", stale))
        val service = Service()
        val repo = SearchRepository(service = service, searchDao = dao)

        val result = repo.autocomplete("term")

        assertEquals(listOf("net"), result.terms)
        assertTrue(dao.deleted)
        assertEquals(1, service.calls)
    }
}
