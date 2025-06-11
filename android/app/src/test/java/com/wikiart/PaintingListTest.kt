import org.junit.Assert.assertEquals
import org.junit.Test

class PaintingListTest {
    @Test
    fun pageCountHandlesZeroPaintings() {
        val list = PaintingList(emptyList(), allPaintingsCount = 0, pageSize = 5)
        assertEquals(0, list.pageCount)
    }

    @Test
    fun pageCountExactMultiple() {
        val list = PaintingList(emptyList(), allPaintingsCount = 10, pageSize = 5)
        assertEquals(2, list.pageCount)
    }

    @Test
    fun pageCountRoundsUp() {
        val list = PaintingList(emptyList(), allPaintingsCount = 9, pageSize = 5)
        assertEquals(2, list.pageCount)
    }
}
