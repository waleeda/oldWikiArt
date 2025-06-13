package com.wikiart

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import coil.load
import com.github.chrisbanes.photoview.PhotoView

class ImageDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.setBackgroundBlurRadius(
                resources.getDimensionPixelSize(R.dimen.detail_blur_radius)
            )
        }
        val url = intent.getStringExtra(EXTRA_IMAGE_URL) ?: ""
        enterImmersiveMode()
        setContent {
            ImageDetailScreen(imageUrl = url, onClose = { finish() })
        }
    }

    override fun finish() {
        exitImmersiveMode()
        super.finish()
    }

    private fun enterImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.hide(WindowInsets.Type.systemBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        }
    }

    private fun exitImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.systemBars())
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    @Composable
    private fun ImageDetailScreen(imageUrl: String, onClose: () -> Unit) {
        AndroidView(
            factory = { context ->
                PhotoView(context).apply {
                    layoutParams = android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    load(imageUrl) { allowHardware(false) }
                    setOnClickListener { onClose() }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }

    companion object {
        const val EXTRA_IMAGE_URL = "extra_image_url"
    }
}
