package com.wikiart.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a grouping within an artist category.
 */
data class ArtistSection(
    @SerializedName("CategoryId") val categoryId: CategoryId,
    @SerializedName("Url") val url: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Count") val count: Int
) {
    data class CategoryId(@SerializedName("_oid") val oid: String)
}

/** Wrapper for the API response. */
data class ArtistSectionsResponse(
    @SerializedName("DictionariesWithCategories") val dictionaries: Map<String, List<ArtistSection>>
) {
    val items: List<ArtistSection> get() = dictionaries.values.flatten()
}
