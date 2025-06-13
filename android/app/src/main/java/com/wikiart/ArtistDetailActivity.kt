package com.wikiart

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.ActivityOptions
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.ImageView
import android.widget.LinearLayout
import com.wikiart.RelatedPaintingAdapter
import coil.load
import kotlinx.coroutines.launch
import android.view.View

class ArtistDetailActivity : AppCompatActivity() {
    private val adapter = RelatedPaintingAdapter { painting, image ->
        val intent = android.content.Intent(this, PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
        val options = ActivityOptions.makeSceneTransitionAnimation(
            this,
            image,
            image.transitionName
        )
        startActivity(intent, options.toBundle())
    }

    private val repository by lazy { PaintingRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_detail)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.setBackgroundBlurRadius(
                resources.getDimensionPixelSize(R.dimen.detail_blur_radius)
            )
        }

        val recycler: RecyclerView = findViewById(R.id.famousRecyclerView)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter

        val detailsContainer: LinearLayout = findViewById(R.id.detailsContainer)

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
                findViewById<ImageView>(R.id.artistImage).load(details.image)
                addDetails(detailsContainer, details)
            }
            val paintings = repository.getFamousPaintings(artistUrl)
            adapter.submitList(paintings)
        }
    }

    private fun addDetails(container: LinearLayout, details: ArtistDetails) {
        details.biography?.takeIf { it.isNotBlank() }?.let {
            addDetail(container, getString(R.string.biography_label), it)
        }
        details.gender?.takeIf { it.isNotBlank() }?.let {
            addDetail(container, getString(R.string.gender_label), it)
        }
        details.series?.takeIf { it.isNotBlank() }?.let {
            addDetail(container, getString(R.string.series_label), it)
        }
        details.themes?.takeIf { it.isNotBlank() }?.let {
            addDetail(container, getString(R.string.themes_label), it)
        }
        details.periods?.takeIf { it.isNotBlank() }?.let {
            addDetail(container, getString(R.string.periods_label), it)
        }
        details.birth?.takeIf { it.isNotBlank() }?.let {
            addDetail(container, getString(R.string.birth_label), it)
        }
        details.death?.takeIf { it.isNotBlank() }?.let {
            addDetail(container, getString(R.string.death_label), it)
        }
    }

    private fun addDetail(container: LinearLayout, label: String, value: String) {
        val view = layoutInflater.inflate(R.layout.item_painting_detail_field, container, false)
        view.findViewById<TextView>(R.id.detailLabel).text = label
        view.findViewById<TextView>(R.id.detailValue).text = value
        container.addView(view)
    }

    companion object {
        const val EXTRA_ARTIST_URL = "extra_artist_url"
        const val EXTRA_ARTIST_NAME = "extra_artist_name"
    }
}
