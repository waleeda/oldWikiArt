package com.example.wikiart.ui.paintings

import com.example.wikiart.api.ApiClient
import com.example.wikiart.api.WikiArtService
import com.example.wikiart.model.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PaintingListViewModelTest {

    private class FakeService : WikiArtService {
        var lastPageRequested = 0
        override suspend fun popularPaintings(language: String, page: Int, json: Int): PaintingList {
            lastPageRequested = page
            val p = Painting(
                id = "popular_$page",
                title = "Title$page",
                year = "",
                width = 0,
                height = 0,
                artistName = "Artist",
                image = "url",
                paintingUrl = "",
                artistUrl = null,
                flags = 0
            )
            return PaintingList(listOf(p), 1, 1)
        }
        override suspend fun paintingsByCategory(language: String, param: String, page: Int, json: Int): PaintingList {
            lastPageRequested = page
            val p = Painting(
                id = page.toString(),
                title = "Title$page",
                year = "",
                width = 0,
                height = 0,
                artistName = "Artist",
                image = "url",
                paintingUrl = "",
                artistUrl = null,
                flags = 0
            )
            return PaintingList(listOf(p), 1, 1)
        }
        override suspend fun paintingDetails(language: String, id: String): Painting {
            throw NotImplementedError()
        }

        override suspend fun relatedPaintings(path: String, json: Int): PaintingList {
            throw NotImplementedError()
        }

        override suspend fun artistDetails(path: String, json: Int): ArtistDetails {
            throw NotImplementedError()
        }

        override suspend fun artistPaintings(path: String, json: Int, page: Int): PaintingList {
            throw NotImplementedError()
        }

        override suspend fun artistsByCategory(language: String, category: String, page: Int, json: Int, layout: String, searchTerm: String?): ArtistList {
            throw NotImplementedError()
        }

        override suspend fun artistSections(language: String, category: String, json: Int): ArtistSections {
            throw NotImplementedError()
        }

        override suspend fun paintingSections(language: String, group: Int): List<PaintingSection> {
            return emptyList()
        }

        override suspend fun paintingsBySection(language: String, dictIdsJson: String, page: Int, json: Int): PaintingList {
            lastPageRequested = page
            val p = Painting(
                id = dictIdsJson + page,
                title = "Title$page",
                year = "",
                width = 0,
                height = 0,
                artistName = "Artist",
                image = "url",
                paintingUrl = "",
                artistUrl = null,
                flags = 0
            )
            return PaintingList(listOf(p), 1, 1)
        }

        override suspend fun searchPaintings(language: String, term: String, page: Int, json: Int, layout: String): PaintingList {
            throw NotImplementedError()
        }

        override suspend fun searchArtists(language: String, term: String, page: Int, json: Int, layout: String): ArtistList {
            throw NotImplementedError()
        }

        override suspend fun autocomplete(language: String, term: String, json: Int): AutocompleteResult {
            throw NotImplementedError()
        }
    }

    private val service = FakeService()

    @Before
    fun setup() {
        ApiClient.serviceOverride = service
    }

    @After
    fun tearDown() {
        ApiClient.serviceOverride = null
    }

    @Test
    fun loadNextAppendsResults() = runBlocking {
        val vm = PaintingListViewModel()
        vm.loadNext()
        assertEquals(1, vm.paintings.value!!.size)
        vm.loadNext()
        assertEquals(2, vm.paintings.value!!.size)
        assertEquals(2, service.lastPageRequested)
    }

    @Test
    fun setCategoryResetsList() = runBlocking {
        val vm = PaintingListViewModel()
        vm.loadNext()
        vm.setCategory(PaintingCategory.HIGH_RES)
        assertEquals(PaintingCategory.HIGH_RES, vm.category)
        assertEquals(1, service.lastPageRequested)
        assertEquals(1, vm.paintings.value!!.size)
    }

    @Test
    fun setLayoutUpdatesLayout() {
        val vm = PaintingListViewModel()
        vm.setLayout(PaintingAdapter.Layout.GRID)
        assertEquals(PaintingAdapter.Layout.GRID, vm.layout)
    }
}
