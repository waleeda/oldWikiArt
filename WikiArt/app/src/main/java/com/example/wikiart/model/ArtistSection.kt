package com.example.wikiart.model

import java.io.Serializable

/**
 * Represents a section within an artist category returned by the API.
 */
data class ArtistSection(
    val Url: String,
    val Title: String,
    val Count: Int
) : Serializable {
    val url: String
        get() = Url.substringAfterLast('/')
}

/**
 * API response wrapper for artist sections. The sections are grouped by a dictionary
 * but the app only cares about the flattened list.
 */
data class ArtistSections(
    val DictionariesWithCategories: Map<String, List<ArtistSection>>
) {
    val items: List<ArtistSection>
        get() = DictionariesWithCategories.values.flatten()
}

