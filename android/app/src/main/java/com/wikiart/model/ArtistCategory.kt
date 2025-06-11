package com.wikiart.model

/**
 * Categories for browsing artists.
 * Mirrors the iOS ArtistCategory enum.
 */
enum class ArtistCategory(val path: String, private val hasSections: Boolean) {
    ALPHABETICAL("Alphabet", false),
    ART_MOVEMENT("artists-by-Art-Movement", true),
    SCHOOL("artists-by-painting-school", true),
    GENRE("artists-by-genre", true),
    FIELD("artists-by-field", true),
    NATION("artists-by-nation", true),
    CENTURIES("artists-by-century", true),
    CHRONOLOGY("chronological-artists", false),
    POPULAR("popular-artists", false),
    FEMALE("female-artists", false),
    RECENT("recently-added-artists", false),
    INSTITUTIONS("artists-by-art-institution", true);

    fun hasSections(): Boolean = hasSections

    override fun toString(): String = when (this) {
        ALPHABETICAL -> "Alphabet"
        ART_MOVEMENT -> "Art Movement"
        SCHOOL -> "Painting School"
        GENRE -> "Genre"
        FIELD -> "Field"
        NATION -> "Nation"
        CENTURIES -> "Century"
        CHRONOLOGY -> "Chronological"
        POPULAR -> "Popular"
        FEMALE -> "Female"
        RECENT -> "Recent"
        INSTITUTIONS -> "Art Institution"
    }
}
