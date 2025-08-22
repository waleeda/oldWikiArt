package com.example.wikiart.api

import com.example.wikiart.data.local.CACHE_TIMEOUT
import com.example.wikiart.data.local.PaintingDao
import com.example.wikiart.data.local.PaintingEntity
import com.example.wikiart.data.local.toEntity
import com.example.wikiart.model.Painting
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PaintingDetailsRepositoryTest {

    private class FakePaintingDao(initial: PaintingEntity?) : PaintingDao {
        var entity: PaintingEntity? = initial
        var deleted = false
        override suspend fun getPainting(id: String): PaintingEntity? = entity
        override suspend fun insert(painting: PaintingEntity) { entity = painting }
        override suspend fun delete(id: String) { deleted = true; entity = null }
    }

    private class Service : FakeWikiArtService() {
        var calls = 0
        override suspend fun paintingDetails(language: String, id: String): Painting {
            calls++
            return Painting(id, "net", "", 0, 0, "artist", "img", "url", null, 0)
        }
    }

    @Test
    fun stalePaintingIsRefreshed() = runBlocking {
        val now = System.currentTimeMillis()
        val stale = now - CACHE_TIMEOUT - 1
        val cachedModel = Painting("1", "cached", "", 0, 0, "artist", "img", "url", null, 0)
        val dao = FakePaintingDao(cachedModel.toEntity(stale))
        val service = Service()
        val repo = PaintingDetailsRepository(service = service, paintingDao = dao)

        val result = repo.getPainting("1")

        assertEquals("net", result.title)
        assertTrue(dao.deleted)
        assertEquals(1, service.calls)
    }
}
