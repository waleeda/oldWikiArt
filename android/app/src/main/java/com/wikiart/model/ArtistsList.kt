package com.wikiart.model

import com.google.gson.annotations.SerializedName

/**
 * API response containing a paginated list of artists and optional paintings.
 */
data class ArtistsList(
    @SerializedName("Artists") val artists: List<Artist>,
    @SerializedName("AllArtistsCount") val allArtistsCount: Int,
    @SerializedName("PageSize") val pageSize: Int?
) {
    val pageCount: Int
        get() = if (pageSize != null && pageSize > 0) (allArtistsCount + pageSize - 1) / pageSize else 1
}
