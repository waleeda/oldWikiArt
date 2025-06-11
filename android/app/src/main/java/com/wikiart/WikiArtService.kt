package com.wikiart

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class WikiArtService(
    private val serverConfig: ServerConfigType = ServerConfig.production,
    private val language: String = "en"
) {
    private val client = OkHttpClient()
    private val gson = Gson()

    fun fetchFeaturedPaintings(page: Int = 1): PaintingList? {
        val url = serverConfig.apiBaseUrl.toString() + "/" + language + "?json=2&param=featured&page=" + page
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            return gson.fromJson(body, PaintingList::class.java)
        }
    }

    fun searchPaintings(term: String, page: Int = 1): PaintingList? {
        val cleaned = java.net.URLEncoder.encode(term, "UTF-8")
        val url = serverConfig.apiBaseUrl.toString() + "/" + language + "/Search/" + cleaned + "?json=2&pageSize=20&page=" + page
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            return gson.fromJson(body, PaintingList::class.java)
        }
    }

    fun searchAutoComplete(term: String): List<SearchAutoComplete>? {
        val url = serverConfig.apiBaseUrl.toString() + "/" + language + "/app/search/autocomplete/?term=" + java.net.URLEncoder.encode(term, "UTF-8")
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            val arr = gson.fromJson(body, Array<SearchAutoComplete>::class.java)
            return arr.toList()
        }
    }
}
