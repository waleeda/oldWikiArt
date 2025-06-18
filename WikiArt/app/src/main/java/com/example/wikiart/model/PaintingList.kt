package com.example.wikiart.model

data class PaintingList(
    val Paintings: List<Painting>,
    val AllPaintingsCount: Int,
    val PageSize: Int
)
