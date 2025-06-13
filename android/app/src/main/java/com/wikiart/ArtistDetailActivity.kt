package com.wikiart

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ArtistDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.setBackgroundBlurRadius(
                resources.getDimensionPixelSize(R.dimen.detail_blur_radius)
            )
        }

        val artistUrl = intent.getStringExtra(EXTRA_ARTIST_URL) ?: return
        val artistName = intent.getStringExtra(EXTRA_ARTIST_NAME) ?: ""

        setContent {
            ArtistDetailScreen(
                artistUrl = artistUrl,
                artistName = artistName,
                onPaintingClick = { painting ->
                    val intent = Intent(this, PaintingDetailActivity::class.java)
                    intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
                    startActivity(intent)
                },
                onSeeAll = {
                    val intent = Intent(this, ArtistPaintingsActivity::class.java)
                    intent.putExtra(ArtistPaintingsActivity.EXTRA_ARTIST_URL, artistUrl)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            )
        }
    }

    companion object {
        const val EXTRA_ARTIST_URL = "extra_artist_url"
        const val EXTRA_ARTIST_NAME = "extra_artist_name"
    }
}
