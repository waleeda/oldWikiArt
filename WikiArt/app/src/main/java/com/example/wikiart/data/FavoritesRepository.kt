package com.example.wikiart.data

import android.content.Context
import android.content.SharedPreferences

class FavoritesRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isFavorite(id: String): Boolean {
        val set = prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
        return set.contains(id)
    }

    /**
     * Toggle favorite state for the given painting id.
     * @return true if the painting is now a favorite, false otherwise.
     */
    fun toggleFavorite(id: String): Boolean {
        val set = prefs.getStringSet(KEY_FAVORITES, emptySet())?.toMutableSet() ?: mutableSetOf()
        val added: Boolean = if (set.contains(id)) {
            set.remove(id)
            false
        } else {
            set.add(id)
            true
        }
        prefs.edit().putStringSet(KEY_FAVORITES, set).apply()
        return added
    }

    companion object {
        private const val PREFS_NAME = "favorites"
        private const val KEY_FAVORITES = "painting_ids"
    }
}

