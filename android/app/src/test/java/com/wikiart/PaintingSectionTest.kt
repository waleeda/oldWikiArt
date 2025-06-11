import com.wikiart.model.PaintingSection
import org.junit.Assert.assertEquals
import org.junit.Test

class PaintingSectionTest {
    @Test
    fun titleForLanguageFallbacksToEnglish() {
        val map = mapOf("en" to "English Title", "fr" to "Titre Francais")
        val section = PaintingSection(
            PaintingSection.Id("1"),
            PaintingSection.Content(
                PaintingSection.Content.Title(map)
            )
        )
        assertEquals("English Title", section.titleForLanguage("de"))
    }
}
