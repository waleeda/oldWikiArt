package com.wikiart

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import android.util.Log
import com.wikiart.BuildConfig
import com.wikiart.model.PaintingSection
import com.wikiart.model.Artist
import com.wikiart.model.ArtistCategory
import com.wikiart.model.ArtistSection
import com.wikiart.model.ArtistSectionsResponse
import com.wikiart.model.ArtistsList

class WikiArtService(
    private val serverConfig: ServerConfigType = ServerConfig.production,
    private val language: String = java.util.Locale.getDefault().language
) {
    companion object {
        private val sharedClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                })
                .build()
        }
    }

    private val client = sharedClient
    private val gson = Gson()
    private val TAG = "WikiArtService"


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
            Log.d(TAG, "fetchPaintings response: $body")
            val result = gson.fromJson(body, PaintingList::class.java)
            Log.d(TAG, "fetchPaintings decoded: $result")
            return result
        }
    }

    fun searchPaintings(term: String, page: Int = 1): PaintingList? {
        val cleaned = java.net.URLEncoder.encode(term, "UTF-8")
        val url = serverConfig.apiBaseUrl.toString() + "/" + language + "/Search/" + cleaned + "?json=2&pageSize=20&page=" + page
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            Log.d(TAG, "searchPaintings response: $body")
            val result = gson.fromJson(body, PaintingList::class.java)
            Log.d(TAG, "searchPaintings decoded: $result")
            return result
        }
    }

    fun searchAutoComplete(term: String): List<SearchAutoComplete>? {
        val url = serverConfig.apiBaseUrl.toString() + "/" + language + "/app/search/autocomplete/?term=" + java.net.URLEncoder.encode(term, "UTF-8")
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            Log.d(TAG, "searchAutoComplete response: $body")
            val arr = gson.fromJson(body, Array<SearchAutoComplete>::class.java)
            Log.d(TAG, "searchAutoComplete decoded: ${arr.toList()}")
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
            Log.d(TAG, "fetchRelatedPaintings response: $body")
            val result = gson.fromJson(body, PaintingList::class.java)
            Log.d(TAG, "fetchRelatedPaintings decoded: $result")
            return result
        }
    }
    fun fetchArtistDetails(path: String): ArtistDetails? {
        val url = "${serverConfig.apiBaseUrl}$path?json=2"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            Log.d(TAG, "fetchArtistDetails response: $body")
            val result = gson.fromJson(body, ArtistDetails::class.java)
            Log.d(TAG, "fetchArtistDetails decoded: $result")
            return result
        }
    }

    fun fetchFamousPaintings(path: String, page: Int = 1): PaintingList? {
        val url = "${serverConfig.apiBaseUrl}$path/mode/featured?page=$page&json=2"

        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            Log.d(TAG, "fetchFamousPaintings response: $body")
            val result = gson.fromJson(body, PaintingList::class.java)
            Log.d(TAG, "fetchFamousPaintings decoded: $result")
            return result
        }
    }

    fun fetchAllPaintingsByDate(path: String, page: Int = 1): PaintingList? {
        val cleanPath = if (path.startsWith("http")) path else "${serverConfig.apiBaseUrl}$path"
        val url = "$cleanPath/all-works?page=$page&json=2"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            Log.d(TAG, "fetchAllPaintingsByDate response: $body")
            val result = gson.fromJson(body, PaintingList::class.java)
            Log.d(TAG, "fetchAllPaintingsByDate decoded: $result")
            return result
        }
    }

    fun fetchAllPaintingsByAlphabet(path: String, page: Int = 1): PaintingList? {
        val cleanPath = if (path.startsWith("http")) path else "${serverConfig.apiBaseUrl}$path"
        val url = "$cleanPath/mode/all-paintings-by-alphabet?page=$page&json=2"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            Log.d(TAG, "fetchAllPaintingsByAlphabet response: $body")
            val result = gson.fromJson(body, PaintingList::class.java)
            Log.d(TAG, "fetchAllPaintingsByAlphabet decoded: $result")
            return result
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
            Log.d(TAG, "fetchSections response: $body")
            val arr = gson.fromJson(body, Array<PaintingSection>::class.java)
            Log.d(TAG, "fetchSections decoded: ${arr.toList()}")
            return arr.toList()
        }
    }

    fun fetchArtistSections(category: ArtistCategory): List<ArtistSection>? {
        val url = "${serverConfig.apiBaseUrl}/$language/App/Search/${category.path}?json=2"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            Log.d(TAG, "fetchArtistSections response: $body")
            val res = gson.fromJson(body, ArtistSectionsResponse::class.java)
            Log.d(TAG, "fetchArtistSections decoded: ${res.items}")
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
            Log.d(TAG, "fetchArtists response: $body")
            val result = gson.fromJson(body, ArtistsList::class.java)
            Log.d(TAG, "fetchArtists decoded: $result")
            return result
        }
    }

    fun fetchArtistsList(path: String, page: Int = 1): ArtistsList? {
        val cleanPath = if (path.startsWith("http")) path else "${serverConfig.apiBaseUrl}$path"
        val url = "$cleanPath?page=$page&json=3&layout=new"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            Log.d(TAG, "fetchArtistsList response: $body")
            val result = gson.fromJson(body, ArtistsList::class.java)
            Log.d(TAG, "fetchArtistsList decoded: $result")
            return result
        }
    }

}
