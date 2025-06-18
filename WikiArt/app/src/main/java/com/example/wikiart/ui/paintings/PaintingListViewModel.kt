package com.example.wikiart.ui.paintings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wikiart.api.ApiClient
import com.example.wikiart.model.Painting
import com.example.wikiart.model.PaintingCategory
import kotlinx.coroutines.launch

class PaintingListViewModel : ViewModel() {
    private val _paintings = MutableLiveData<List<Painting>>(emptyList())
    val paintings: LiveData<List<Painting>> = _paintings

    private var page = 1
    private var loading = false
    var category: PaintingCategory = PaintingCategory.POPULAR
        private set
    var layout: PaintingAdapter.Layout = PaintingAdapter.Layout.LIST
        private set

    fun loadNext() {
        if (loading) return
        loading = true
        viewModelScope.launch {
            try {
                if (category == PaintingCategory.FAVORITES) {
                    // TODO: load favourites from local storage when implemented
                    loading = false
                    return@launch
                }

                val result = ApiClient.service.paintingsByCategory(
                    language = "en",
                    param = category.param,
                    page = page
                )
                _paintings.value = _paintings.value!! + result.Paintings
                page++
            } catch (_: Exception) {
            }
            loading = false
        }
    }

    fun setCategory(cat: PaintingCategory) {
        if (category == cat) return
        category = cat
        page = 1
        _paintings.value = emptyList()
        loadNext()
    }

    fun setLayout(l: PaintingAdapter.Layout) {
        layout = l
    }
}
