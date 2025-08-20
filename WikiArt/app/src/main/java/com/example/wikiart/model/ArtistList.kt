package com.example.wikiart.model

data class ArtistList(
    val Artists: List<Artist>,
    val AllArtistsCount: Int,
    val PageSize: Int
)
