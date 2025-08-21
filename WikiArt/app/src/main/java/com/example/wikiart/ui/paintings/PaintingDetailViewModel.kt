package com.example.wikiart.ui.paintings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wikiart.api.PaintingDetailsRepository
import com.example.wikiart.api.RelatedPaintingsRepository
import com.example.wikiart.api.getLanguage
import com.example.wikiart.model.Painting
import kotlinx.coroutines.launch

class PaintingDetailViewModel(
    private val paintingId: String,
    private val language: String = getLanguage(),
    private val detailsRepository: PaintingDetailsRepository = PaintingDetailsRepository(language),
    private val relatedRepository: RelatedPaintingsRepository = RelatedPaintingsRepository(),
) : ViewModel() {

    private val _painting = MutableLiveData<Painting?>()
    val painting: LiveData<Painting?> = _painting

    private val _related = MutableLiveData<List<Painting>>(emptyList())
    val related: LiveData<List<Painting>> = _related

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val details = detailsRepository.getPainting(paintingId)
                _painting.value = details
                val rel = relatedRepository.getRelated(details.paintingUrl)
                _related.value = rel
            } catch (_: Exception) {
            }
            _loading.value = false
        }
    }

    class Factory(
        private val paintingId: String,
        private val language: String = getLanguage(),
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PaintingDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PaintingDetailViewModel(paintingId, language) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
