package com.example.wikiart.api

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.favoritesDataStore by preferencesDataStore(name = "favorites")

class FavoritesRepository(private val context: Context) {
    private val dataStore = context.favoritesDataStore
    private val key = stringSetPreferencesKey("painting_ids")

    suspend fun isFavorite(id: String): Boolean {
        val set = dataStore.data.first()[key] ?: emptySet()
        return set.contains(id)
    }

    /**
     * Toggle favorite state for the given painting id.
     * @return true if the painting is now a favorite, false otherwise.
     */
    suspend fun toggleFavorite(id: String): Boolean {
        var added = false
        dataStore.edit { prefs ->
            val set = prefs[key]?.toMutableSet() ?: mutableSetOf()
            added = if (set.contains(id)) {
                set.remove(id)
                false
            } else {
                set.add(id)
                true
            }
            prefs[key] = set
        }
        return added
    }

    suspend fun getFavorites(): Set<String> {
        return dataStore.data.first()[key] ?: emptySet()
    }
}
