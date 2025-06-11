package com.wikiart

import com.google.gson.annotations.SerializedName

data class PaintingList(
    @SerializedName("Paintings") val paintings: List<Painting>,
    @SerializedName("AllPaintingsCount") val allPaintingsCount: Int,
    @SerializedName("PageSize") val pageSize: Int
) {
    val pageCount: Int
        get() = (allPaintingsCount + pageSize - 1) / pageSize
}
