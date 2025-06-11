import androidx.paging.PagingSource
import com.wikiart.WikiArtPagingSource
import com.wikiart.WikiArtService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WikiArtPagingSourceTest {
    @Test
    fun loadHandlesEmptyResponse() = runBlocking {
        val service = mockk<WikiArtService>()
        coEvery { service.fetchPaintings(PaintingCategory.FEATURED, 1, null) } returns
            PaintingList(emptyList(), 0, 20)

        val source = WikiArtPagingSource(service, PaintingCategory.FEATURED)
        val params = PagingSource.LoadParams.Refresh<Int>(null, 20, false)
        val result = source.load(params)

        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        assertTrue(result.data.isEmpty())
        assertEquals(null, result.prevKey)
        assertEquals(null, result.nextKey)
    }

    @Test
    fun loadReturnsError() = runBlocking {
        val service = mockk<WikiArtService>()
        coEvery { service.fetchPaintings(PaintingCategory.FEATURED, 1, null) } throws RuntimeException("boom")

        val source = WikiArtPagingSource(service, PaintingCategory.FEATURED)
        val params = PagingSource.LoadParams.Refresh<Int>(null, 20, false)
        val result = source.load(params)

        assertTrue(result is PagingSource.LoadResult.Error)
    }
}
