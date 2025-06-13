package com.wikiart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ArtistPaintingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val artistUrl = intent.getStringExtra(EXTRA_ARTIST_URL) ?: return
        setContent {
            ArtistPaintingsScreen(
                artistUrl = artistUrl,
                onPaintingClick = { painting ->
                    val intent = Intent(this, PaintingDetailActivity::class.java)
                    intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
                    startActivity(intent)
                }
            )
        }
    }

    companion object {
        const val EXTRA_ARTIST_URL = "extra_artist_url"
    }
}
