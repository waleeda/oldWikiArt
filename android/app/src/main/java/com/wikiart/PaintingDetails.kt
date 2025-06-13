package com.wikiart

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Detailed painting information returned from the API.
 */
data class PaintingDetails(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("artistUrl") val artistUrl: String?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("artistId") val artistId: String?,
    @SerializedName("completitionYear") val completionYear: Int?,
    @SerializedName("dictionaries") val dictionaries: List<String>?,
    @SerializedName("location") val location: String?,
    @SerializedName("period") val period: String?,
    @SerializedName("serie") val series: String?,
    @SerializedName("genres") val genres: List<String>?,
    @SerializedName("styles") val styles: List<String>?,
    @SerializedName("media") val media: List<String>?,
    @SerializedName("sizeX") val sizeX: Float?,
    @SerializedName("sizeY") val sizeY: Float?,
    @SerializedName("diameter") val diameter: Double?,
    @SerializedName("galleries") val galleries: List<String>?,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("description") val description: String?,
    @SerializedName("width") val width: Int?,
    @SerializedName("image") val image: String?,
    @SerializedName("height") val height: Int?
) : Serializable
