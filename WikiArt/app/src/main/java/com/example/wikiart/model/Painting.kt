package com.example.wikiart.model

data class Painting(
    val id: String,
    val title: String,
    val year: String,
    val width: Int,
    val height: Int,
    val artistName: String,
    val image: String,
    val paintingUrl: String,
    val artistUrl: String?,
    val flags: Int
)
{
    fun imageUrl(size: String = ""): String = image + size
}
