package com.example.wikiart.ui.artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wikiart.api.ArtistsRepository
import com.example.wikiart.api.ArtistsCategoryRepository
import com.example.wikiart.model.Artist
import com.example.wikiart.model.ArtistCategory
import com.example.wikiart.model.ArtistSection
import kotlinx.coroutines.launch

class ArtistListViewModel : ViewModel() {
    private val repository = ArtistsRepository()
    private val categoryRepository = ArtistsCategoryRepository()

    private val _artists = MutableLiveData<List<Artist>>(emptyList())
    val artists: LiveData<List<Artist>> = _artists

    private val _sections = MutableLiveData<List<ArtistSection>>(emptyList())
    val sections: LiveData<List<ArtistSection>> = _sections

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private var page = 1
    var category: ArtistCategory = ArtistCategory.POPULAR
        private set
    var section: ArtistSection? = null
        private set
    var layout: ArtistAdapter.Layout = ArtistAdapter.Layout.LIST
        private set

    init {
        loadSections()
    }

    fun loadNext() {
        if (_loading.value == true) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val result = repository.getArtists(category, page, section?.url)
                _artists.value = _artists.value!! + result.Artists
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
        section = null
        _artists.value = emptyList()
        loadSections()
        loadNext()
    }

    fun setSection(sec: ArtistSection?) {
        if (section == sec) return
        section = sec
        page = 1
        _artists.value = emptyList()
        loadNext()
    }

    fun setLayout(l: ArtistAdapter.Layout) {
        layout = l
    }

    private fun loadSections() {
        viewModelScope.launch {
            try {
                _sections.value = categoryRepository.getSections(category)
            } catch (_: Exception) {
                _sections.value = emptyList()
            }
        }
    }
}
