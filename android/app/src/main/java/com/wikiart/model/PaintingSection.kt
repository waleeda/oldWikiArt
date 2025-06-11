package com.wikiart.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a subcategory section for MEDIA, STYLE or GENRE.
 */
data class PaintingSection(
    @SerializedName("_id") val id: Id,
    @SerializedName("Content") val content: Content
) {
    data class Id(
        @SerializedName("_oid") val oid: String
    )
    data class Content(
        @SerializedName("Title") val title: Title
    ) {
        data class Title(
            @SerializedName("Title") val titles: Map<String, String>
        )
    }

    /**
     * Returns the localized title using the provided language code,
     * falling back to English.
     */
    fun titleForLanguage(lang: String): String {
        val map = content.title.titles
        return map[lang] ?: map["en"] ?: map.values.firstOrNull() ?: ""
    }
}
