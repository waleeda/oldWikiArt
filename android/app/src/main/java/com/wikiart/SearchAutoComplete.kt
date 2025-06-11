package com.wikiart

import com.google.gson.annotations.SerializedName

data class SearchAutoComplete(
    @SerializedName("Url") val url: String,
    @SerializedName("Value") val value: String,
    @SerializedName("Label") val label: String,
    @SerializedName("Description") val description: String,
    @SerializedName("Image") val image: String?
)
