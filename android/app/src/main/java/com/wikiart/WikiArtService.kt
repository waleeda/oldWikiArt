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


    fun fetchFeaturedPaintings(page: Int = 1): PaintingList? =
        fetchPaintings(PaintingCategory.FEATURED, page)

    fun fetchPaintings(category: PaintingCategory, page: Int = 1): PaintingList? {
        val url = when (category) {
            PaintingCategory.FEATURED ->
                "${serverConfig.apiBaseUrl}/$language?json=2&param=featured&page=$page"
            PaintingCategory.POPULAR ->
                "${serverConfig.apiBaseUrl}/$language/popular-paintings/alltime?page=$page&json=2"
            PaintingCategory.HIGH_RES ->
                "${serverConfig.apiBaseUrl}/$language?json=2&param=high_resolution&page=$page"
            else ->
                "${serverConfig.apiBaseUrl}/$language?json=2&param=featured&page=$page"
        }

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

    fun searchArtists(term: String, page: Int = 1): ArtistsList? {
        val cleaned = java.net.URLEncoder.encode(term, "UTF-8")
        val url = serverConfig.apiBaseUrl.toString() + "/" + language + "/Search/" + cleaned +
            "?json=3&layout=new&resultType=masonry&pageSize=20&page=" + page
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            return gson.fromJson(body, ArtistsList::class.java)
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
