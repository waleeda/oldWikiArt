package com.example.wikiart.ui.artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wikiart.api.ArtistPaintingsRepository
import com.example.wikiart.model.Painting
import kotlinx.coroutines.launch

class ArtistPaintingsViewModel(
    private val artistPath: String,
    private val repository: ArtistPaintingsRepository = ArtistPaintingsRepository(),
) : ViewModel() {

    private val _paintings = MutableLiveData<List<Painting>>(emptyList())
    val paintings: LiveData<List<Painting>> = _paintings

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private var page = 1

    fun loadNext() {
        if (_loading.value == true) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val result = repository.getPaintings(artistPath, page)
                _paintings.value = _paintings.value!! + result
                page++
            } catch (e: Exception) {
                _error.value = e
            }
            _loading.value = false
        }
    }

    class Factory(private val artistPath: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistPaintingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistPaintingsViewModel(artistPath) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
