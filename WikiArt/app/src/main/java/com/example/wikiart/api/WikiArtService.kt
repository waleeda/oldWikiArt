package com.example.wikiart.api

import com.example.wikiart.model.Painting
import com.example.wikiart.model.PaintingList
import com.example.wikiart.model.ArtistList
import com.example.wikiart.model.ArtistDetails
import com.example.wikiart.model.AutocompleteResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

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

    @GET
    suspend fun relatedPaintings(
        @Url path: String,
        @Query("json") json: Int = 2,
    ): PaintingList

    @GET
    suspend fun artistDetails(
        @Url path: String,
        @Query("json") json: Int = 2,
    ): ArtistDetails

    @GET
    suspend fun artistPaintings(
        @Url path: String,
        @Query("json") json: Int = 2,
        @Query("page") page: Int = 1,
    ): PaintingList

    @GET("/{lang}/App/Search/{category}")
    suspend fun artistsByCategory(
        @Path("lang") language: String,
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("json") json: Int = 3,
        @Query("layout") layout: String = "new"
    ): ArtistList

    @GET("/{lang}/App/Search/Paintings")
    suspend fun searchPaintings(
        @Path("lang") language: String,
        @Query("term") term: String,
        @Query("page") page: Int,
        @Query("json") json: Int = 3,
        @Query("layout") layout: String = "new"
    ): PaintingList

    @GET("/{lang}/App/Search/Artists")
    suspend fun searchArtists(
        @Path("lang") language: String,
        @Query("term") term: String,
        @Query("page") page: Int,
        @Query("json") json: Int = 3,
        @Query("layout") layout: String = "new"
    ): ArtistList

    @GET("/{lang}/autocomplete")
    suspend fun autocomplete(
        @Path("lang") language: String,
        @Query("term") term: String,
        @Query("json") json: Int = 1
    ): AutocompleteResult
}
