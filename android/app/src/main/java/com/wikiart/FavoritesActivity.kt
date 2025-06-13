package com.wikiart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


class FavoritesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            FavoritesScreen(onItemClick = { painting ->
                val intent = Intent(this, PaintingDetailActivity::class.java)
                intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
                startActivity(intent)
            })

        }
    }

}
