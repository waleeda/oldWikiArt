package com.example.wikiart.ui.artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wikiart.api.ArtistDetailsRepository
import com.example.wikiart.model.Painting
import kotlinx.coroutines.launch

class ArtistDetailViewModel(
    private val artistPath: String,
    private val repository: ArtistDetailsRepository = ArtistDetailsRepository(),
) : ViewModel() {

    private val _biography = MutableLiveData<String?>()
    val biography: LiveData<String?> = _biography

    private val _paintings = MutableLiveData<List<Painting>>(emptyList())
    val paintings: LiveData<List<Painting>> = _paintings

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val details = repository.getDetails(artistPath)
                _biography.value = details.biography
                val arts = repository.getPaintings(artistPath)
                _paintings.value = arts
            } catch (_: Exception) {
            }
            _loading.value = false
        }
    }

    class Factory(private val artistPath: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistDetailViewModel(artistPath) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
