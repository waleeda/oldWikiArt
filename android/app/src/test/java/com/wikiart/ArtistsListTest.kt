import com.wikiart.model.ArtistsList
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistsListTest {
    @Test
    fun pageCountNullPageSize() {
        val list = ArtistsList(emptyList(), allArtistsCount = 10, pageSize = null)
        assertEquals(1, list.pageCount)
    }

    @Test
    fun pageCountZeroPageSize() {
        val list = ArtistsList(emptyList(), allArtistsCount = 10, pageSize = 0)
        assertEquals(1, list.pageCount)
    }

    @Test
    fun pageCountZeroArtists() {
        val list = ArtistsList(emptyList(), allArtistsCount = 0, pageSize = 5)
        assertEquals(0, list.pageCount)
    }

    @Test
    fun pageCountExactMultiple() {
        val list = ArtistsList(emptyList(), allArtistsCount = 10, pageSize = 5)
        assertEquals(2, list.pageCount)
    }

    @Test
    fun pageCountRoundsUp() {
        val list = ArtistsList(emptyList(), allArtistsCount = 9, pageSize = 5)
        assertEquals(2, list.pageCount)
    }
}
