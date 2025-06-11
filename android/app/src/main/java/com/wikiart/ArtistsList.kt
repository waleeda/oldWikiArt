package com.wikiart

import com.google.gson.annotations.SerializedName

// Response from artist search API
// Only fields required by the app are modeled

data class ArtistsList(
    @SerializedName("Artists") val artists: List<Artist>,
    @SerializedName("AllArtistsCount") val allArtistsCount: Int,
    @SerializedName("PageSize") val pageSize: Int?
) {
    val pageCount: Int
        get() = if (pageSize == null || pageSize == 0) 1 else (allArtistsCount / pageSize) + 1
}
