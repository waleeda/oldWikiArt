package com.example.wikiart.api

import com.example.wikiart.model.*

open class FakeWikiArtService : WikiArtService {
    override suspend fun popularPaintings(language: String, page: Int, json: Int): PaintingList =
        throw NotImplementedError()

    override suspend fun paintingsByCategory(language: String, param: String, page: Int, json: Int): PaintingList =
        throw NotImplementedError()

    override suspend fun paintingDetails(language: String, id: String): Painting =
        throw NotImplementedError()

    override suspend fun relatedPaintings(path: String, json: Int): PaintingList =
        throw NotImplementedError()

    override suspend fun artistDetails(path: String, json: Int): ArtistDetails =
        throw NotImplementedError()

    override suspend fun artistPaintings(path: String, json: Int, page: Int): PaintingList =
        throw NotImplementedError()

    override suspend fun artistsByCategory(language: String, category: String, page: Int, json: Int, layout: String, searchTerm: String?): ArtistList =
        throw NotImplementedError()

    override suspend fun artistSections(language: String, category: String, json: Int): ArtistSections =
        throw NotImplementedError()

    override suspend fun paintingSections(language: String, group: Int): List<PaintingSection> =
        throw NotImplementedError()

    override suspend fun paintingsBySection(language: String, dictIdsJson: String, page: Int, json: Int): PaintingList =
        throw NotImplementedError()

    override suspend fun searchPaintings(language: String, term: String, page: Int, json: Int, layout: String): PaintingList =
        throw NotImplementedError()

    override suspend fun searchArtists(language: String, term: String, page: Int, json: Int, layout: String): ArtistList =
        throw NotImplementedError()

    override suspend fun autocomplete(language: String, term: String, json: Int): AutocompleteResult =
        throw NotImplementedError()
}
