package com.example.wikiart.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wikiart.api.SearchRepository
import com.example.wikiart.model.Artist
import com.example.wikiart.model.Painting
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = SearchRepository()

    private val paintings = mutableListOf<Painting>()
    private val artists = mutableListOf<Artist>()
    private val suggestions = MutableLiveData<List<String>>(emptyList())

    private val _items = MutableLiveData<List<SearchItem>>(emptyList())
    val items: LiveData<List<SearchItem>> = _items

    private var paintingsPage = 1
    private var artistsPage = 1
    private var query: String = ""
    private var loadingPaintings = false
    private var loadingArtists = false

    fun search(term: String) {
        query = term
        paintings.clear()
        artists.clear()
        paintingsPage = 1
        artistsPage = 1
        suggestions.value = emptyList()
        buildItems()
        loadMorePaintings()
        loadMoreArtists()
    }

    fun loadMorePaintings() {
        if (loadingPaintings || query.isEmpty()) return
        loadingPaintings = true
        viewModelScope.launch {
            try {
                val result = repository.searchPaintings(query, paintingsPage)
                paintings.addAll(result.Paintings)
                paintingsPage++
                buildItems()
            } finally {
                loadingPaintings = false
            }
        }
    }

    fun loadMoreArtists() {
        if (loadingArtists || query.isEmpty()) return
        loadingArtists = true
        viewModelScope.launch {
            try {
                val result = repository.searchArtists(query, artistsPage)
                artists.addAll(result.Artists)
                artistsPage++
                buildItems()
            } finally {
                loadingArtists = false
            }
        }
    }

    fun autocomplete(term: String) {
        viewModelScope.launch {
            if (term.isEmpty()) {
                suggestions.value = emptyList()
                buildItems()
                return@launch
            }
            try {
                val result = repository.autocomplete(term)
                suggestions.value = result.terms
            } catch (_: Exception) {
                suggestions.value = emptyList()
            }
            buildItems()
        }
    }

    private fun buildItems() {
        val data = mutableListOf<SearchItem>()
        val sugg = suggestions.value ?: emptyList()
        if (sugg.isNotEmpty()) {
            data.add(SearchItem.Header("Suggestions"))
            data.addAll(sugg.map { SearchItem.SuggestionItem(it) })
        }
        if (paintings.isNotEmpty()) {
            data.add(SearchItem.Header("Paintings"))
            data.addAll(paintings.map { SearchItem.PaintingItem(it) })
        }
        if (artists.isNotEmpty()) {
            data.add(SearchItem.Header("Artists"))
            data.addAll(artists.map { SearchItem.ArtistItem(it) })
        }
        _items.postValue(data)
    }
}
