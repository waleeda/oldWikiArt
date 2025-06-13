package com.wikiart.model

import com.wikiart.R
import com.wikiart.CategoryItem

/**
 * Categories for browsing artists.
 * Mirrors the iOS ArtistCategory enum.
 */
enum class ArtistCategory(val path: String, private val hasSections: Boolean) : CategoryItem {
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

    fun nameRes(): Int = when (this) {
        ALPHABETICAL -> R.string.artist_category_alphabet
        ART_MOVEMENT -> R.string.artist_category_art_movement
        SCHOOL -> R.string.artist_category_school
        GENRE -> R.string.artist_category_genre
        FIELD -> R.string.artist_category_field
        NATION -> R.string.artist_category_nation
        CENTURIES -> R.string.artist_category_century
        CHRONOLOGY -> R.string.artist_category_chronology
        POPULAR -> R.string.artist_category_popular
        FEMALE -> R.string.artist_category_female
        RECENT -> R.string.artist_category_recent
        INSTITUTIONS -> R.string.artist_category_institution
    }

    fun iconRes(): Int = R.drawable.ic_artists
}
