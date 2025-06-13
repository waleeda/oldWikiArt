package com.wikiart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.ActivityOptions
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.ImageView
import com.wikiart.RelatedPaintingAdapter
import coil.load
import kotlinx.coroutines.launch
import android.view.View

class ArtistDetailActivity : AppCompatActivity() {
    private val adapter = RelatedPaintingAdapter { painting ->
        val intent = android.content.Intent(this, PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
        val options = ActivityOptions.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private var bioExpanded = false

    private val repository = PaintingRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_detail)

        val recycler: RecyclerView = findViewById(R.id.famousRecyclerView)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter

        val bioView: TextView = findViewById(R.id.artistBio)
        val birthView: TextView = findViewById(R.id.artistBirth)
        val deathView: TextView = findViewById(R.id.artistDeath)
        val bioToggle: TextView = findViewById(R.id.bioToggle)
        bioToggle.setOnClickListener {
            bioExpanded = !bioExpanded
            bioView.maxLines = if (bioExpanded) Int.MAX_VALUE else 3
            birthView.visibility = if (bioExpanded && birthView.text.isNotBlank()) View.VISIBLE else View.GONE
            deathView.visibility = if (bioExpanded && deathView.text.isNotBlank()) View.VISIBLE else View.GONE
            bioToggle.text = getString(if (bioExpanded) R.string.show_less else R.string.show_more)
        }

        val artistUrl = intent.getStringExtra(EXTRA_ARTIST_URL) ?: return
        val artistName = intent.getStringExtra(EXTRA_ARTIST_NAME) ?: ""

        findViewById<com.google.android.material.button.MaterialButton>(R.id.seeAllButton).setOnClickListener {
            val intent = android.content.Intent(this, ArtistPaintingsActivity::class.java)
            intent.putExtra(ArtistPaintingsActivity.EXTRA_ARTIST_URL, artistUrl)
            val options = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(intent, options.toBundle())
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        findViewById<TextView>(R.id.artistName).text = artistName

        lifecycleScope.launch {
            val details = repository.getArtistDetails(artistUrl)
            if (details != null) {
                findViewById<TextView>(R.id.artistName).text = details.artistName
                bioView.text = details.biography
                birthView.text = details.birth?.let { getString(R.string.born, it) } ?: ""
                deathView.text = details.death?.let { getString(R.string.died, it) } ?: ""
                birthView.visibility = if (bioExpanded && birthView.text.isNotBlank()) View.VISIBLE else View.GONE
                deathView.visibility = if (bioExpanded && deathView.text.isNotBlank()) View.VISIBLE else View.GONE
                findViewById<ImageView>(R.id.artistImage).load(details.image)
                bioView.post {
                    if (bioView.lineCount <= 3 && birthView.text.isBlank() && deathView.text.isBlank()) {
                        bioToggle.visibility = View.GONE
                    }
                }
            }
            val paintings = repository.getFamousPaintings(artistUrl)
            adapter.submitList(paintings)
        }
    }

    companion object {
        const val EXTRA_ARTIST_URL = "extra_artist_url"
        const val EXTRA_ARTIST_NAME = "extra_artist_name"
    }
}
