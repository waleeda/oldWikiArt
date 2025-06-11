package com.wikiart

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.wikiart.model.PaintingSection

class PaintingRepository(private val service: WikiArtService = WikiArtService()) {

    suspend fun getPaintings(
        category: PaintingCategory,
        page: Int = 1,
        sectionId: String? = null
    ): List<Painting> =
        withContext(Dispatchers.IO) {
            service.fetchPaintings(category, page, sectionId)?.paintings ?: emptyList()
        }

    fun pagingFlow(category: PaintingCategory, sectionId: String? = null): Flow<PagingData<Painting>> =
        Pager(PagingConfig(pageSize = 20)) {
            WikiArtPagingSource(service, category, sectionId)
        }.flow

    suspend fun autoComplete(term: String): List<SearchAutoComplete> =
        withContext(Dispatchers.IO) {
            service.searchAutoComplete(term) ?: emptyList()
        }

    fun searchPagingFlow(term: String): Flow<PagingData<Painting>> =
        Pager(PagingConfig(pageSize = 20)) {
            SearchPagingSource(service, term)
        }.flow

    suspend fun sections(category: PaintingCategory): List<PaintingSection> =
        withContext(Dispatchers.IO) {
            service.fetchSections(category) ?: emptyList()
        }
}
