package com.example.wikiart.model

/**
 * Response wrapper for artist list requests.
 * Only the list of [Artist] objects is used at the moment but
 * additional fields mirror the painting list structure for future use.
 */
data class ArtistList(
    val Artists: List<Artist>,
    val AllArtistsCount: Int? = null,
    val PageSize: Int? = null
)
