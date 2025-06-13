package com.wikiart

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

class StoreActivity : ComponentActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL) ?: ""
        setContent {
            StoreScreen(imageUrl)
        }
    }

    @Composable
    private fun StoreScreen(imageUrl: String) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl("https://www.canvaspop.com/upload?imageUrl=$imageUrl")
            }
        })
    }

    companion object {
        const val EXTRA_IMAGE_URL = "extra_image_url"
    }
}
