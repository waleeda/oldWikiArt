package com.example.wikiart.ui.artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wikiart.model.Artist
import com.example.wikiart.model.ArtistCategory
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for displaying a paged list of artists.
 */
class ArtistListViewModel(
    private val repository: ArtistsRepository = ArtistsRepository()
) : ViewModel() {

    private val _artists = MutableLiveData<List<Artist>>(emptyList())
    val artists: LiveData<List<Artist>> = _artists

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    var category: ArtistCategory = ArtistCategory.POPULAR
        private set

    var layout: ArtistAdapter.Layout = ArtistAdapter.Layout.LIST
        private set

    private var page = 1

    /**
     * Load the next page of artists from the repository.
     */
    fun loadNext() {
        if (_loading.value == true) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val result = repository.loadArtists(page, category)
                _artists.value = _artists.value!! + result
                page++
            } catch (e: Exception) {
                _error.value = e
            }
            _loading.value = false
        }
    }

    fun setCategory(cat: ArtistCategory) {
        if (category == cat) return
        category = cat
        page = 1
        _artists.value = emptyList()
        loadNext()
    }

    fun setLayout(l: ArtistAdapter.Layout) {
        layout = l
    }
}
