package com.example.wikiart.api

import com.example.wikiart.model.PaintingCategory
import com.example.wikiart.model.PaintingSection

/**
 * Repository providing painting sections for a given category.
 */
class PaintingSectionsRepository(
    private val service: WikiArtService = ApiClient.service,
    private val language: String = getLanguage(),
) {
    suspend fun getSections(category: PaintingCategory): List<PaintingSection> {
        val group = when (category) {
            PaintingCategory.STYLE -> 2
            PaintingCategory.GENRE -> 3
            PaintingCategory.MEDIA -> 12
            else -> return emptyList()
        }
        return service.paintingSections(
            language = language,
            group = group,
        )
    }
}

