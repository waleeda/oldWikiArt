package com.example.wikiart

import android.app.Application
import com.google.android.material.color.DynamicColors

class WikiArtApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
