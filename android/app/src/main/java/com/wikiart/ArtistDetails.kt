package com.wikiart

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ArtistDetails(
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("biography") val biography: String?,
    @SerializedName("birthDayAsString") val birth: String?,
    @SerializedName("deathDayAsString") val death: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("url") val url: String?
) : Serializable
