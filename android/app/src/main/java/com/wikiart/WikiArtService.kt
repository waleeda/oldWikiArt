package com.wikiart

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import com.wikiart.model.PaintingSection
import com.wikiart.model.Artist
import com.wikiart.model.ArtistCategory
import com.wikiart.model.ArtistSection
import com.wikiart.model.ArtistSectionsResponse
import com.wikiart.model.ArtistsList

class WikiArtService(
    private val serverConfig: ServerConfigType = ServerConfig.production,
    private val language: String = "en"
) {
    private val client = OkHttpClient()
    private val gson = Gson()


    fun fetchFeaturedPaintings(page: Int = 1): PaintingList? =
        fetchPaintings(PaintingCategory.FEATURED, page)

    fun fetchPaintings(
        category: PaintingCategory,
        page: Int = 1,
        sectionId: String? = null
    ): PaintingList? {
        val url = when (category) {
            PaintingCategory.FEATURED ->
                "${serverConfig.apiBaseUrl}/$language?json=2&param=featured&page=$page"
            PaintingCategory.POPULAR ->
                "${serverConfig.apiBaseUrl}/$language/popular-paintings/alltime?page=$page&json=2"
            PaintingCategory.HIGH_RES ->
                "${serverConfig.apiBaseUrl}/$language?json=2&param=high_resolution&page=$page"
            PaintingCategory.MEDIA,
            PaintingCategory.STYLE,
            PaintingCategory.GENRE -> {
                val id = sectionId ?: return null
                val dict = "[%22$id%22]"
                "${serverConfig.apiBaseUrl}/$language/app/Search/PaintingAdvancedSearch?json=2&page=$page&dictIdsJson=$dict"
            }
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


    fun fetchRelatedPaintings(path: String): PaintingList? {
        val cleanPath = if (path.startsWith("http")) path else "${serverConfig.apiBaseUrl}$path"
        val url = "$cleanPath?json=2"
      val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            return gson.fromJson(body, PaintingList::class.java)
        }
    }
    fun fetchArtistDetails(path: String): ArtistDetails? {
        val url = "${serverConfig.apiBaseUrl}$path?json=2"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            return gson.fromJson(body, ArtistDetails::class.java)
        }
    }

    fun fetchFamousPaintings(path: String, page: Int = 1): PaintingList? {
        val url = "${serverConfig.apiBaseUrl}$path/mode/featured?page=$page&json=2"

        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            return gson.fromJson(body, PaintingList::class.java)
        }
    }


    fun fetchSections(category: PaintingCategory): List<PaintingSection>? {
        val group = when (category) {
            PaintingCategory.MEDIA -> 12
            PaintingCategory.STYLE -> 2
            PaintingCategory.GENRE -> 3
            else -> return null
        }
        val url = "${serverConfig.apiBaseUrl}/$language/app/dictionary/GetAllGroup?group=$group"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            val arr = gson.fromJson(body, Array<PaintingSection>::class.java)
            return arr.toList()
        }
    }

    fun fetchArtistSections(category: ArtistCategory): List<ArtistSection>? {
        val url = "${serverConfig.apiBaseUrl}/$language/App/Search/${category.path}?json=2"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            val res = gson.fromJson(body, ArtistSectionsResponse::class.java)
            return res.items
        }
    }

    fun fetchArtists(
        category: ArtistCategory,
        page: Int = 1,
        section: String? = null
    ): ArtistsList? {
        val encoded = section?.let { java.net.URLEncoder.encode(it, "UTF-8") } ?: ""
        val url = "${serverConfig.apiBaseUrl}/$language/App/Search/${category.path}?json=3&layout=new&page=$page&searchterm=$encoded"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            return gson.fromJson(body, ArtistsList::class.java)
        }
    }

    fun fetchArtistsList(path: String, page: Int = 1): ArtistsList? {
        val cleanPath = if (path.startsWith("http")) path else "${serverConfig.apiBaseUrl}$path"
        val url = "$cleanPath?page=$page&json=3&layout=new"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            return gson.fromJson(body, ArtistsList::class.java)
        }
    }

}
