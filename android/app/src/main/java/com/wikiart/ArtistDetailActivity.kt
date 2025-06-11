package com.wikiart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.ActivityOptions
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.ImageView
import coil.load
import kotlinx.coroutines.launch
import androidx.paging.PagingData

class ArtistDetailActivity : AppCompatActivity() {
    private val adapter = PaintingAdapter { painting ->
        val intent = android.content.Intent(this, PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
        val options = ActivityOptions.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private val repository = PaintingRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_detail)

        val recycler: RecyclerView = findViewById(R.id.famousRecyclerView)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        val artistUrl = intent.getStringExtra(EXTRA_ARTIST_URL) ?: return
        val artistName = intent.getStringExtra(EXTRA_ARTIST_NAME) ?: ""

        findViewById<TextView>(R.id.artistName).text = artistName

        lifecycleScope.launch {
            val details = repository.getArtistDetails(artistUrl)
            if (details != null) {
                findViewById<TextView>(R.id.artistName).text = details.artistName
                findViewById<TextView>(R.id.artistBio).text = details.biography
                findViewById<ImageView>(R.id.artistImage).load(details.image)
            }
            val paintings = repository.getFamousPaintings(artistUrl)
            adapter.submitData(PagingData.from(paintings))
        }
    }

    companion object {
        const val EXTRA_ARTIST_URL = "extra_artist_url"
        const val EXTRA_ARTIST_NAME = "extra_artist_name"
    }
}
