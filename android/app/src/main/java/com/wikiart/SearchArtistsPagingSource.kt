package com.wikiart

import androidx.paging.PagingSource
import androidx.paging.PagingState

class SearchArtistsPagingSource(
    private val service: WikiArtService,
    private val term: String
) : PagingSource<Int, Artist>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        return try {
            val page = params.key ?: 1
            val result = service.searchArtists(term, page)
            val artists = result?.artists ?: emptyList()
            val nextKey = if (page < (result?.pageCount ?: page)) page + 1 else null
            LoadResult.Page(
                data = artists,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
