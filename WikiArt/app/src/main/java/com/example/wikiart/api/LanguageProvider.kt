package com.example.wikiart.api

import java.util.Locale

private val supportedLanguages = setOf("en", "de", "es", "fr", "it", "pt", "ru", "zh", "ja")

fun getLanguage(): String {
    val localeLang = Locale.getDefault().language
    return if (supportedLanguages.contains(localeLang)) localeLang else "en"
}

