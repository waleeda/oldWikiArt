package com.wikiart

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load

class ArtistDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_detail)

        val artist = intent.getSerializableExtra(EXTRA_ARTIST) as? Artist
        findViewById<TextView>(R.id.artistName).text = artist?.title
        artist?.image?.let { url ->
            findViewById<ImageView>(R.id.artistImage).load(url)
        }
    }

    companion object {
        const val EXTRA_ARTIST = "extra_artist"
    }
}
