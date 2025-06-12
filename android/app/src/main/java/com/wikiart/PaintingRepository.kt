package com.wikiart

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.wikiart.model.PaintingSection
import com.wikiart.model.Artist
import com.wikiart.model.ArtistCategory
import com.wikiart.model.ArtistSection

class PaintingRepository(private val service: WikiArtService = WikiArtService()) {

    private val pageSize = 20
    private val prefetchDistance = 5
    private val pagingConfig = PagingConfig(
        pageSize = pageSize,
        prefetchDistance = prefetchDistance,
        initialLoadSize = pageSize
    )

    suspend fun getPaintings(
        category: PaintingCategory,
        page: Int = 1,
        sectionId: String? = null
    ): List<Painting> =
        withContext(Dispatchers.IO) {
            service.fetchPaintings(category, page, sectionId)?.paintings ?: emptyList()
        }

    fun pagingFlow(category: PaintingCategory, sectionId: String? = null): Flow<PagingData<Painting>> =
        Pager(pagingConfig) {
            WikiArtPagingSource(service, category, sectionId)
        }.flow

    suspend fun autoComplete(term: String): List<SearchAutoComplete> =
        withContext(Dispatchers.IO) {
            service.searchAutoComplete(term) ?: emptyList()
        }

    fun searchPagingFlow(term: String): Flow<PagingData<Painting>> =
        Pager(pagingConfig) {
            SearchPagingSource(service, term)
        }.flow


    suspend fun getRelatedPaintings(path: String): List<Painting> =
        withContext(Dispatchers.IO) {
            service.fetchRelatedPaintings(path)?.paintings ?: emptyList()
        }
    suspend fun sections(category: PaintingCategory): List<PaintingSection> =
        withContext(Dispatchers.IO) {
            service.fetchSections(category) ?: emptyList()
        }

    suspend fun getArtistDetails(path: String): ArtistDetails? =
        withContext(Dispatchers.IO) {
            service.fetchArtistDetails(path)
        }

    suspend fun getFamousPaintings(path: String): List<Painting> =
        withContext(Dispatchers.IO) {
            service.fetchFamousPaintings(path)?.paintings ?: emptyList()
        }

    fun artistsPagingFlow(category: ArtistCategory, section: String? = null): Flow<PagingData<Artist>> =
        Pager(pagingConfig) {
            ArtistsPagingSource(service, category, section)
        }.flow
    fun artistPaintingsPagingFlow(
        path: String,
        sort: com.wikiart.model.ArtistPaintingSort = com.wikiart.model.ArtistPaintingSort.DATE
    ): Flow<PagingData<Painting>> =
        Pager(pagingConfig) {
            ArtistPaintingsPagingSource(service, path, sort)
        }.flow


    suspend fun artistSections(category: ArtistCategory): List<ArtistSection> =
        withContext(Dispatchers.IO) {
            service.fetchArtistSections(category) ?: emptyList()
        }
}
