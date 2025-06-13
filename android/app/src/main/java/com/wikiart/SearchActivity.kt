package com.wikiart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchScreen(
                onPaintingClick = { painting ->
                    val intent = Intent(this, PaintingDetailActivity::class.java)
                    intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
                    startActivity(intent)
                },
                onArtistClick = { artist ->
                    val intent = Intent(this, ArtistDetailActivity::class.java)
                    intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_URL, artist.artistUrl)
                    intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_NAME, artist.title)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            )
        }
    }
}
