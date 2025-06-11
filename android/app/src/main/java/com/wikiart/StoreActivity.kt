package com.wikiart

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class StoreActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val webView: WebView = findViewById(R.id.storeWebView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL) ?: ""
        val url = "https://www.canvaspop.com/upload?imageUrl=$imageUrl"
        webView.loadUrl(url)
    }

    companion object {
        const val EXTRA_IMAGE_URL = "extra_image_url"
    }
}
