package com.wikiart.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Basic artist information returned from the API.
 */
data class Artist(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("nation") val nation: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("artistUrl") val artistUrl: String?,
    @SerializedName("totalWorksTitle") val totalWorksTitle: String?
) : Serializable
