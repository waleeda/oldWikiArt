package com.wikiart

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "favorite_paintings")
data class Painting(
    @PrimaryKey
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("year") val year: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("image") val image: String,
    @SerializedName("paintingUrl") val paintingUrl: String,
    @SerializedName("artistUrl") val artistUrl: String?,
    @SerializedName("flags") val flags: Int
) : Serializable {

    private val extension: String
        get() = image.substringAfterLast('.', "")

    /**
     * URL for a smaller thumbnail used in list views.
     */
    val thumbUrl: String
        get() = "${image}!PinterestSmall.$extension"

    /**
     * URL for a larger image used in detail views.
     */
    val detailUrl: String
        get() = "${image}!Blog.$extension"

    /**
     * URL for the original full size image.
     */
    val fullUrl: String
        get() = image
}
