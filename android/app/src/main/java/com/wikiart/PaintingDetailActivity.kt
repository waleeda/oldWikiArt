package com.wikiart

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.graphics.Color
import android.view.Window
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import androidx.activity.compose.setContent

class PaintingDetailActivity : ComponentActivity() {

    private val repository by lazy { PaintingRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.setBackgroundBlurRadius(
                resources.getDimensionPixelSize(R.dimen.detail_blur_radius)
            )
        }
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 300
            scrimColor = Color.TRANSPARENT
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 300
            scrimColor = Color.TRANSPARENT
        }
        val painting = intent.getSerializableExtra(EXTRA_PAINTING) as? Painting ?: Painting(
            id = "temp",
            title = intent.getStringExtra(EXTRA_TITLE) ?: "",
            year = "",
            width = 0,
            height = 0,
            artistName = intent.getStringExtra(EXTRA_ARTIST_NAME) ?: "",
            image = intent.getStringExtra(EXTRA_IMAGE) ?: "",
            paintingUrl = "",
            artistUrl = intent.getStringExtra(EXTRA_ARTIST_URL),
            flags = 0,
        )

        setContent {
            PaintingDetailScreen(
                painting = painting,
                onRelatedClick = { selected ->
                    val intent = Intent(this, PaintingDetailActivity::class.java)
                    intent.putExtra(EXTRA_PAINTING, selected)
                    startActivity(intent)
                },
                onArtistClick = { url, name ->
                    val intent = Intent(this, ArtistDetailActivity::class.java)
                    intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_URL, url)
                    intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_NAME, name)
                    startActivity(intent)
                },
                onShare = { p ->
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "https://${ServerConfig.production.apiBaseUrl.host}${'$'}{p.paintingUrl}")
                    }
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
                },
                onBuy = { p ->
                    val intent = Intent(this, StoreActivity::class.java)
                    intent.putExtra(StoreActivity.EXTRA_IMAGE_URL, p.fullUrl)
                    startActivity(intent)
                }
            )
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
