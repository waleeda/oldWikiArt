package com.wikiart

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import android.view.View
import android.app.ActivityOptions
import com.wikiart.ImageDetailActivity
import coil.load
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wikiart.data.FavoritesRepository
import kotlinx.coroutines.launch

class PaintingDetailActivity : AppCompatActivity() {

    private val repository by lazy { PaintingRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painting_detail)

        val painting = intent.getSerializableExtra(EXTRA_PAINTING) as? Painting

        val title = painting?.title ?: intent.getStringExtra(EXTRA_TITLE) ?: ""
        val imageUrl = painting?.detailUrl ?: intent.getStringExtra(EXTRA_IMAGE) ?: ""
        val fullUrl = painting?.fullUrl ?: painting?.image ?: imageUrl

        findViewById<TextView>(R.id.detailTitle).text = title
        val detailImage: ImageView = findViewById(R.id.detailImage)
        detailImage.load(imageUrl)
        detailImage.setOnClickListener {
            val intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra(ImageDetailActivity.EXTRA_IMAGE_URL, fullUrl)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                detailImage,
                "detailImage"
            )
            startActivity(intent, options.toBundle())
        }


        painting?.let {
            val yearView: TextView = findViewById(R.id.detailYear)
            val dimView: TextView = findViewById(R.id.detailDimensions)

            if (it.year.isNotBlank()) {
                yearView.text = getString(R.string.year, it.year)
                yearView.visibility = View.VISIBLE
            } else {
                yearView.visibility = View.GONE
            }

            if (it.width > 0 && it.height > 0) {
                dimView.text = getString(R.string.dimensions, it.width, it.height)
                dimView.visibility = View.VISIBLE
            } else {
                dimView.visibility = View.GONE
            }

        }

        val artistNameView: TextView = findViewById(R.id.detailArtist)
        val artistName = painting?.artistName
        artistNameView.text = artistName
        artistNameView.setOnClickListener {
            val url = painting?.artistUrl ?: return@setOnClickListener
            val intent = Intent(this, ArtistDetailActivity::class.java)
            intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_URL, url)
            intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_NAME, artistName)
            val options = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(intent, options.toBundle())
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }

        val favoriteButton: ImageButton = findViewById(R.id.favoriteButton)
        val shareButton: Button = findViewById(R.id.shareButton)
        val buyButton: Button = findViewById(R.id.buyButton)
        val repo = FavoritesRepository(this)

        lifecycleScope.launch {
            if (painting != null) {
                val fav = repo.isFavorite(painting.id)
                favoriteButton.isSelected = fav
                favoriteButton.contentDescription = if (fav) {
                    getString(R.string.remove_favorite)
                } else {
                    getString(R.string.add_favorite)
                }
                favoriteButton.jumpDrawablesToCurrentState()
            }
        }

        favoriteButton.setOnClickListener {
            painting ?: return@setOnClickListener
            lifecycleScope.launch {
                val currentlyFav = repo.isFavorite(painting.id)
                if (currentlyFav) {
                    repo.removeFavorite(painting)
                    Toast.makeText(this@PaintingDetailActivity, R.string.removed_favorite, Toast.LENGTH_SHORT).show()
                } else {
                    repo.addFavorite(painting)
                    Toast.makeText(this@PaintingDetailActivity, R.string.added_favorite, Toast.LENGTH_SHORT).show()
                }
                favoriteButton.isSelected = !currentlyFav
                favoriteButton.contentDescription = if (!currentlyFav) {
                    getString(R.string.remove_favorite)
                } else {
                    getString(R.string.add_favorite)
                }
                favoriteButton.jumpDrawablesToCurrentState()
            }
        }

        shareButton.setOnClickListener {
            painting ?: return@setOnClickListener
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "https://${ServerConfig.production.apiBaseUrl.host}${painting.paintingUrl}")
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
        }

        buyButton.setOnClickListener {
            painting ?: return@setOnClickListener
            val intent = Intent(this, StoreActivity::class.java)
            intent.putExtra(StoreActivity.EXTRA_IMAGE_URL, painting.fullUrl)
            val options = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(intent, options.toBundle())
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        val relatedRecycler: RecyclerView = findViewById(R.id.relatedRecyclerView)
        val relatedTitle: TextView = findViewById(R.id.relatedTitle)
        val relatedAdapter = RelatedPaintingAdapter { selected ->
            val intent = Intent(this, PaintingDetailActivity::class.java)
            intent.putExtra(EXTRA_PAINTING, selected)
            val options = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(intent, options.toBundle())
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        relatedRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        relatedRecycler.adapter = relatedAdapter

        lifecycleScope.launch {
            painting?.let {
                val related = repository.getRelatedPaintings(it.paintingUrl)
                if (related.isNotEmpty()) {
                    relatedAdapter.submitList(related)
                    relatedTitle.visibility = View.VISIBLE
                }
                else {
                    relatedTitle.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_PAINTING = "extra_painting"
        const val EXTRA_ARTIST_URL = "extra_artist_url"
        const val EXTRA_ARTIST_NAME = "extra_artist_name"
    }
}
