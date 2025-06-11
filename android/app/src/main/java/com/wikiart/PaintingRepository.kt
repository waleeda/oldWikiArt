package com.wikiart

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PaintingRepository(private val service: WikiArtService = WikiArtService()) {
    suspend fun getFeaturedPaintings(page: Int = 1): List<Painting> =
        withContext(Dispatchers.IO) {
            service.fetchFeaturedPaintings(page)?.paintings ?: emptyList()
        }

    fun pagingFlow(): Flow<PagingData<Painting>> =
        Pager(PagingConfig(pageSize = 20)) {
            WikiArtPagingSource(service)
        }.flow
}
