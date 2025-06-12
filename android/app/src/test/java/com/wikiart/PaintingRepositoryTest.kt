import androidx.paging.Pager
import com.wikiart.PaintingRepository
import com.wikiart.PaintingCategory
import com.wikiart.WikiArtPagingSource
import com.wikiart.WikiArtService
import com.wikiart.ArtistPaintingsPagingSource
import com.wikiart.model.ArtistPaintingSort
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PaintingRepositoryTest {
    @Test
    fun pagingFlowCreatesWikiArtPagingSource() {
        val service = mockk<WikiArtService>(relaxed = true)
        val repo = PaintingRepository(service)
        val flow = repo.pagingFlow(PaintingCategory.FEATURED, "section")

        val field = flow.javaClass.getDeclaredField("this$0")
        field.isAccessible = true
        val pager = field.get(flow) as Pager<*, *>
        val source = pager.pagingSourceFactory.invoke()
        assertTrue(source is WikiArtPagingSource)

        val serviceField = WikiArtPagingSource::class.java.getDeclaredField("service")
        val categoryField = WikiArtPagingSource::class.java.getDeclaredField("category")
        val sectionField = WikiArtPagingSource::class.java.getDeclaredField("sectionId")
        serviceField.isAccessible = true
        categoryField.isAccessible = true
        sectionField.isAccessible = true
        assertEquals(service, serviceField.get(source))
        assertEquals(PaintingCategory.FEATURED, categoryField.get(source))
        assertEquals("section", sectionField.get(source))
    }
    @Test
    fun artistPaintingsPagingFlowCreatesSource() {
        val service = mockk<WikiArtService>(relaxed = true)
        val repo = PaintingRepository(service)
        val flow = repo.artistPaintingsPagingFlow("/foo", ArtistPaintingSort.DATE)

        val field = flow.javaClass.getDeclaredField("this$0")
        field.isAccessible = true
        val pager = field.get(flow) as Pager<*, *>
        val source = pager.pagingSourceFactory.invoke()
        assertTrue(source is ArtistPaintingsPagingSource)

        val serviceField = ArtistPaintingsPagingSource::class.java.getDeclaredField("service")
        val pathField = ArtistPaintingsPagingSource::class.java.getDeclaredField("path")
        val sortField = ArtistPaintingsPagingSource::class.java.getDeclaredField("sort")
        serviceField.isAccessible = true
        pathField.isAccessible = true
        sortField.isAccessible = true
        assertEquals(service, serviceField.get(source))
        assertEquals("/foo", pathField.get(source))
        assertEquals(ArtistPaintingSort.DATE, sortField.get(source))
    }
}
