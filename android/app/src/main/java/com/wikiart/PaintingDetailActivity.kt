package com.wikiart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import coil.load
import androidx.lifecycle.lifecycleScope
import com.wikiart.data.FavoritesRepository
import kotlinx.coroutines.launch

class PaintingDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painting_detail)

        val painting = intent.getSerializableExtra(EXTRA_PAINTING) as? Painting

        val title = painting?.title ?: intent.getStringExtra(EXTRA_TITLE) ?: ""
        val imageUrl = painting?.image ?: intent.getStringExtra(EXTRA_IMAGE) ?: ""

        findViewById<TextView>(R.id.detailTitle).text = title
        findViewById<ImageView>(R.id.detailImage).load(imageUrl)

        val favoriteButton: Button = findViewById(R.id.favoriteButton)
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
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_PAINTING = "extra_painting"
    }
}
