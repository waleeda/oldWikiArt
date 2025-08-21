package com.example.wikiart

import android.app.Application
import com.example.wikiart.data.local.WikiArtDatabase
import com.google.android.material.color.DynamicColors

class WikiArtApplication : Application() {
    val database: WikiArtDatabase by lazy { WikiArtDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    companion object {
        lateinit var instance: WikiArtApplication
            private set
    }
}

