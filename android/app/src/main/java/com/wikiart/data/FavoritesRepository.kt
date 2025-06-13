package com.wikiart.data

import android.content.Context
import com.wikiart.Painting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class FavoritesRepository(context: Context) {
    private val dao = FavoritesDatabase.getDatabase(context).favoritesDao()

    fun favoritesFlow(): Flow<List<Painting>> =
        dao.favoritesFlow().flowOn(Dispatchers.IO)

    suspend fun isFavorite(id: String): Boolean = withContext(Dispatchers.IO) {
        dao.isFavorite(id)
    }

    suspend fun addFavorite(painting: Painting) = withContext(Dispatchers.IO) {
        dao.insert(painting)
    }

    suspend fun removeFavorite(painting: Painting) = withContext(Dispatchers.IO) {
        dao.delete(painting)
    }
}
