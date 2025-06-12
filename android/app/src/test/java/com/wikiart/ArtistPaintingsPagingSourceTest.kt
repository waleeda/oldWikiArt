import androidx.paging.PagingSource
import com.wikiart.ArtistPaintingsPagingSource
import com.wikiart.WikiArtService
import com.wikiart.PaintingList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ArtistPaintingsPagingSourceTest {
    @Test
    fun loadHandlesEmptyResponse() = runBlocking {
        val service = mockk<WikiArtService>()
        coEvery { service.fetchAllPaintingsByDate("/foo", 1) } returns
            PaintingList(emptyList(), 0, 20)

        val source = ArtistPaintingsPagingSource(service, "/foo")
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
        coEvery { service.fetchAllPaintingsByDate("/foo", 1) } throws RuntimeException("boom")

        val source = ArtistPaintingsPagingSource(service, "/foo")
        val params = PagingSource.LoadParams.Refresh<Int>(null, 20, false)
        val result = source.load(params)

        assertTrue(result is PagingSource.LoadResult.Error)
    }
}
