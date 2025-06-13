package com.wikiart

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ArtistDetails(
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("biography") val biography: String?,
    @SerializedName("series") val series: String?,
    @SerializedName("themes") val themes: String?,
    @SerializedName("periodsOfWork") val periods: String?,
    @SerializedName("birthDayAsString") val birth: String?,
    @SerializedName("deathDayAsString") val death: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("url") val url: String?
) : Serializable
