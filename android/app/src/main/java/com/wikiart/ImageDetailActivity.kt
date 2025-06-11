package com.wikiart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.github.chrisbanes.photoview.PhotoView

class ImageDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        val url = intent.getStringExtra(EXTRA_IMAGE_URL) ?: ""
        val imageView: PhotoView = findViewById(R.id.fullImageView)
        imageView.load(url)
        imageView.setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_IMAGE_URL = "extra_image_url"
    }
}
