package com.example.wikiart.model

/**
 * Represents a section within a painting category returned by the API.
 */
data class PaintingSection(
    val _id: Id,
    val content: Content,
) {
    data class Id(val _oid: String)
    data class Content(val Title: TitleContent) {
        data class TitleContent(val Title: Map<String, String>)
    }

    /**
     * Section identifier used in API calls.
     */
    val url: String
        get() = _id._oid

    /**
     * Localized title resolved from the map of translations.
     */
    val title: String
        get() {
            val lang = com.example.wikiart.api.getLanguage()
            return content.Title.Title[lang]
                ?: content.Title.Title["en"]
                ?: content.Title.Title.values.firstOrNull().orEmpty()
        }
}

