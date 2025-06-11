package com.wikiart

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import coil.load
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wikiart.data.FavoritesRepository
import kotlinx.coroutines.launch

class PaintingDetailActivity : AppCompatActivity() {

    private val repository = PaintingRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painting_detail)

        val painting = intent.getSerializableExtra(EXTRA_PAINTING) as? Painting

        val title = painting?.title ?: intent.getStringExtra(EXTRA_TITLE) ?: ""
        val imageUrl = painting?.image ?: intent.getStringExtra(EXTRA_IMAGE) ?: ""

        findViewById<TextView>(R.id.detailTitle).text = title
        findViewById<ImageView>(R.id.detailImage).load(imageUrl)

        painting?.let {
            findViewById<TextView>(R.id.detailYear).text = getString(R.string.year, it.year)
            findViewById<TextView>(R.id.detailDimensions).text =
                getString(R.string.dimensions, it.width, it.height)
        }

        val favoriteButton: Button = findViewById(R.id.favoriteButton)
        val shareButton: Button = findViewById(R.id.shareButton)
        val buyButton: Button = findViewById(R.id.buyButton)
        val repo = FavoritesRepository(this)

        lifecycleScope.launch {
            if (painting != null) {
                val fav = repo.isFavorite(painting.id)
                favoriteButton.text = if (fav) getString(R.string.remove_favorite) else getString(R.string.add_favorite)
            }
        }

        favoriteButton.setOnClickListener {
            painting ?: return@setOnClickListener
            lifecycleScope.launch {
                if (repo.isFavorite(painting.id)) {
                    repo.removeFavorite(painting)
                    favoriteButton.text = getString(R.string.add_favorite)
                    Toast.makeText(this@PaintingDetailActivity, R.string.removed_favorite, Toast.LENGTH_SHORT).show()
                } else {
                    repo.addFavorite(painting)
                    favoriteButton.text = getString(R.string.remove_favorite)
                    Toast.makeText(this@PaintingDetailActivity, R.string.added_favorite, Toast.LENGTH_SHORT).show()
                }
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
            intent.putExtra(StoreActivity.EXTRA_IMAGE_URL, painting.image)
            startActivity(intent)
        }

        val relatedRecycler: RecyclerView = findViewById(R.id.relatedRecyclerView)
        val relatedAdapter = RelatedPaintingAdapter { selected ->
            val intent = Intent(this, PaintingDetailActivity::class.java)
            intent.putExtra(EXTRA_PAINTING, selected)
            startActivity(intent)
        }
        relatedRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        relatedRecycler.adapter = relatedAdapter

        lifecycleScope.launch {
            painting?.let {
                val related = repository.getRelatedPaintings(it.paintingUrl)
                if (related.isNotEmpty()) {
                    relatedAdapter.submitList(related)
                }
            }
        }
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_PAINTING = "extra_painting"
    }
}
