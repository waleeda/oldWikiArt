package com.example.wikiart.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {
    private const val BASE_URL = "https://www.wikiart.org/"

    var serviceOverride: WikiArtService? = null

    private val defaultService: WikiArtService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(WikiArtService::class.java)
    }

    val service: WikiArtService
        get() = serviceOverride ?: defaultService
}
