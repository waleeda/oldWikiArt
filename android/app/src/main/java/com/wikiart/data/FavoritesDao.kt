package com.wikiart.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wikiart.Painting
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite_paintings ORDER BY title")
    fun favoritesFlow(): Flow<List<Painting>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_paintings WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(painting: Painting)

    @Delete
    suspend fun delete(painting: Painting)
}
