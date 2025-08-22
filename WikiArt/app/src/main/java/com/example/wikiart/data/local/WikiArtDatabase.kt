package com.example.wikiart.data.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.wikiart.model.Artist
import com.example.wikiart.model.AutocompleteResult
import com.example.wikiart.model.Painting
import org.json.JSONArray
import java.util.concurrent.TimeUnit

const val CACHE_TIMEOUT: Long = TimeUnit.HOURS.toMillis(1)

@Entity(tableName = "paintings")
data class PaintingEntity(
    @PrimaryKey val id: String,
    val title: String,
    val year: String,
    val width: Int,
    val height: Int,
    val artistName: String,
    val image: String,
    val paintingUrl: String,
    val artistUrl: String?,
    val flags: Int,
    val updated: Long,
) {
    fun toModel(): Painting = Painting(id, title, year, width, height, artistName, image, paintingUrl, artistUrl, flags)
}

fun Painting.toEntity(now: Long): PaintingEntity = PaintingEntity(id, title, year, width, height, artistName, image, paintingUrl, artistUrl, flags, now)

@Entity(tableName = "artists")
data class ArtistEntity(
    @PrimaryKey val id: String,
    val title: String?,
    val year: String?,
    val nation: String?,
    val image: String?,
    val artistUrl: String?,
    val totalWorksTitle: String?,
    val category: String?,
    val page: Int,
    val section: String?,
    val updated: Long,
) {
    fun toModel(): Artist = Artist(id, title, year, nation, image, artistUrl, totalWorksTitle)
}

fun Artist.toEntity(category: String?, page: Int, section: String?, now: Long): ArtistEntity =
    ArtistEntity(id ?: "", title, year, nation, image, artistUrl, totalWorksTitle, category, page, section, now)

@Entity(tableName = "search_results")
data class SearchResultEntity(
    @PrimaryKey val query: String,
    val results: List<String>,
    val updated: Long,
) {
    fun toModel(): AutocompleteResult = AutocompleteResult(results)
}

fun AutocompleteResult.toEntity(query: String, now: Long): SearchResultEntity =
    SearchResultEntity(query, terms, now)

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        val jsonArray = JSONArray()
        list.forEach { jsonArray.put(it) }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        if (data.isEmpty()) return emptyList()
        val jsonArray = JSONArray(data)
        return List(jsonArray.length()) { index -> jsonArray.optString(index) }
    }
}

@Dao
interface PaintingDao {
    @Query("SELECT * FROM paintings WHERE id = :id")
    suspend fun getPainting(id: String): PaintingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(painting: PaintingEntity)
}

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artists WHERE category = :category AND page = :page AND ((section IS NULL AND :section IS NULL) OR section = :section)")
    suspend fun getArtists(category: String, page: Int, section: String?): List<ArtistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(artists: List<ArtistEntity>)
}

@Dao
interface SearchResultDao {
    @Query("SELECT * FROM search_results WHERE query = :query")
    suspend fun get(query: String): SearchResultEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: SearchResultEntity)
}

@Database(
    entities = [PaintingEntity::class, ArtistEntity::class, SearchResultEntity::class],
    version = 2,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class WikiArtDatabase : RoomDatabase() {
    abstract fun paintingDao(): PaintingDao
    abstract fun artistDao(): ArtistDao
    abstract fun searchResultDao(): SearchResultDao

    companion object {
        @Volatile private var INSTANCE: WikiArtDatabase? = null

        fun getInstance(context: Context): WikiArtDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    WikiArtDatabase::class.java,
                    "wikiart.db"
                ).addMigrations(MIGRATION_1_2).build().also { INSTANCE = it }
            }
    }
}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val cursor = database.query("SELECT query, results FROM search_results")
        val update = database.compileStatement("UPDATE search_results SET results = ? WHERE query = ?")
        while (cursor.moveToNext()) {
            val query = cursor.getString(0)
            val results = cursor.getString(1)
            val jsonArray = JSONArray()
            if (results.isNotEmpty()) {
                results.split("|||").forEach { jsonArray.put(it) }
            }
            update.bindString(1, jsonArray.toString())
            update.bindString(2, query)
            update.executeUpdateDelete()
        }
        cursor.close()
    }
}

