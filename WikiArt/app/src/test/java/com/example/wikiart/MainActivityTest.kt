import android.net.Uri
import com.example.wikiart.MainActivity
import com.example.wikiart.R
import com.example.wikiart.ui.artists.ArtistDetailFragment
import com.example.wikiart.ui.paintings.PaintingDetailFragment
import org.junit.Assert.*
import org.junit.Test

class MainActivityTest {
    @Test
    fun parsePaintingDeepLink() {
        val uri = Uri.parse("https://www.wikiart.org/en/paintings/123")
        val request = MainActivity.parseDeepLink(uri)
        assertNotNull(request)
        assertEquals(R.id.paintingDetailFragment, request!!.destinationId)
        assertEquals("123", request.args.getString(PaintingDetailFragment.ARG_PAINTING_ID))
    }

    @Test
    fun parseArtistDeepLink() {
        val uri = Uri.parse("https://www.wikiart.org/en/artists/picasso")
        val request = MainActivity.parseDeepLink(uri)
        assertNotNull(request)
        assertEquals(R.id.artistDetailFragment, request!!.destinationId)
        assertEquals("/en/artists/picasso", request.args.getString(ArtistDetailFragment.ARG_ARTIST_PATH))
    }

    @Test
    fun parseInvalidSchemeReturnsNull() {
        val uri = Uri.parse("http://www.wikiart.org/en/paintings/123")
        val request = MainActivity.parseDeepLink(uri)
        assertNull(request)
    }

    @Test
    fun parseUnknownPathReturnsNull() {
        val uri = Uri.parse("https://www.wikiart.org/en/unknown/123")
        val request = MainActivity.parseDeepLink(uri)
        assertNull(request)
    }
}
