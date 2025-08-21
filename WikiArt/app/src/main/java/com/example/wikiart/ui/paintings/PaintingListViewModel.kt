package com.example.wikiart.ui.paintings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wikiart.api.ApiClient
import com.example.wikiart.api.FavoritesRepository
import com.example.wikiart.api.PaintingSectionsRepository
import com.example.wikiart.api.getLanguage
import com.example.wikiart.model.Painting
import com.example.wikiart.model.PaintingCategory
import com.example.wikiart.model.PaintingSection
import kotlinx.coroutines.launch

class PaintingListViewModel(
    private val favoritesRepository: FavoritesRepository? = null,
    private val language: String = getLanguage(),
) : ViewModel() {
    private val _paintings = MutableLiveData<List<Painting>>(emptyList())
    val paintings: LiveData<List<Painting>> = _paintings

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private var page = 1
    var category: PaintingCategory = PaintingCategory.POPULAR
        private set
    var section: PaintingSection? = null
        private set
    var layout: PaintingAdapter.Layout = PaintingAdapter.Layout.LIST
        private set

    private val sectionsRepository = PaintingSectionsRepository(language)
    private val _sections = MutableLiveData<List<PaintingSection>>(emptyList())
    val sections: LiveData<List<PaintingSection>> = _sections

    fun loadNext() {
        if (_loading.value == true) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                if (category == PaintingCategory.FAVORITES) {
                    val ids = favoritesRepository?.getFavorites()?.toList() ?: emptyList()
                    val favs = ids.mapNotNull { id ->
                        try {
                            ApiClient.service.paintingDetails(language, id)
                        } catch (_: Exception) {
                            null
                        }
                    }
                    _paintings.value = favs
                    _loading.value = false
                    return@launch
                }

                val result = when {
                    category == PaintingCategory.POPULAR -> {
                        ApiClient.service.popularPaintings(
                            language = language,
                            page = page,
                        )
                    }
                    section != null -> {
                        ApiClient.service.paintingsBySection(
                            language = language,
                            dictIdsJson = "[\"${section!!.url}\"]",
                            page = page,
                        )
                    }
                    else -> {
                        ApiClient.service.paintingsByCategory(
                            language = language,
                            param = category.param,
                            page = page,
                        )
                    }
                }
                _paintings.value = _paintings.value!! + result.Paintings
                page++
            } catch (e: Exception) {
                _error.value = e
            }
            _loading.value = false
        }
    }

    fun setCategory(cat: PaintingCategory) {
        if (category == cat) return
        category = cat
        section = null
        page = 1
        _paintings.value = emptyList()
        _sections.value = emptyList()
        viewModelScope.launch {
            _sections.value = sectionsRepository.getSections(cat)
        }
        loadNext()
    }

    fun setSection(sec: PaintingSection?) {
        if (section == sec) return
        section = sec
        page = 1
        _paintings.value = emptyList()
        loadNext()
    }

    fun setLayout(l: PaintingAdapter.Layout) {
        layout = l
    }

    class Factory(
        private val repo: FavoritesRepository,
        private val language: String = getLanguage(),
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PaintingListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PaintingListViewModel(repo, language) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
