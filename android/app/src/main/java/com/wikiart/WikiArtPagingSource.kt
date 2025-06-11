package com.wikiart

import androidx.paging.PagingSource
import androidx.paging.PagingState

class WikiArtPagingSource(
    private val service: WikiArtService
) : PagingSource<Int, Painting>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Painting> {
        return try {
            val page = params.key ?: 1
            val result = service.fetchFeaturedPaintings(page)
            val paintings = result?.paintings ?: emptyList()
            val nextKey = if (page < (result?.pageCount ?: page)) page + 1 else null
            LoadResult.Page(
                data = paintings,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Painting>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
