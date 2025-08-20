package com.example.wikiart.model

import com.example.wikiart.R

enum class ArtistCategory(val path: String, val titleRes: Int) {
    POPULAR("popular-artists", R.string.category_popular),
    FEMALE("female-artists", R.string.category_female),
    RECENT("recently-added-artists", R.string.category_recent)
}
