package com.example.wikiart.api

import com.example.wikiart.WikiArtApplication
import com.example.wikiart.data.local.CACHE_TIMEOUT
import com.example.wikiart.data.local.PaintingDao
import com.example.wikiart.data.local.toEntity
import com.example.wikiart.model.Painting

class PaintingDetailsRepository(
    private val service: WikiArtService = ApiClient.service,
    private val language: String = getLanguage(),
    private val paintingDao: PaintingDao = WikiArtApplication.instance.database.paintingDao(),
) {
    suspend fun getPainting(id: String): Painting {
        val cached = paintingDao.getPainting(id)
        val now = System.currentTimeMillis()
        if (cached != null) {
            if (now - cached.updated < CACHE_TIMEOUT) {
                return cached.toModel()
            } else {
                paintingDao.delete(id)
            }
        }
        val network = service.paintingDetails(language, id)
        paintingDao.insert(network.toEntity(now))
        return network
    }
}

