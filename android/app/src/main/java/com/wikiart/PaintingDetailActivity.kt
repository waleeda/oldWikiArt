package com.wikiart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import coil.load

class PaintingDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painting_detail)

        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE) ?: ""

        findViewById<TextView>(R.id.detailTitle).text = title
        findViewById<ImageView>(R.id.detailImage).load(imageUrl)
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_IMAGE = "extra_image"
    }
}
