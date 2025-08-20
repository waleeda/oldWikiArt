package com.example.wikiart.api

import com.example.wikiart.model.Artist
import com.example.wikiart.model.ArtistList
import com.example.wikiart.model.Painting
import com.example.wikiart.model.PaintingList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WikiArtService {
    @GET("/{lang}/popular-paintings/alltime")
    suspend fun popularPaintings(
        @Path("lang") language: String,
        @Query("page") page: Int,
        @Query("json") json: Int = 2
    ): PaintingList

    @GET("/{lang}")
    suspend fun paintingsByCategory(
        @Path("lang") language: String,
        @Query("param") param: String,
        @Query("page") page: Int,
        @Query("json") json: Int = 2
    ): PaintingList

    @GET("/{lang}/api/2/Painting")
    suspend fun paintingDetails(
        @Path("lang") language: String,
        @Query("id") id: String
    ): Painting

    /**
     * Retrieve a page of artists from the WikiArt API.
     */
    @GET("/{lang}/api/2/Artists")
    suspend fun artists(
        @Path("lang") language: String,
        @Query("page") page: Int,
        @Query("json") json: Int = 2
    ): ArtistList
}
