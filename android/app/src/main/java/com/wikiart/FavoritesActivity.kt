package com.wikiart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage
import com.wikiart.data.FavoritesRepository

class FavoritesActivity : ComponentActivity() {
    private val repository by lazy { FavoritesRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FavoritesScreen { painting ->
                val intent = Intent(this, PaintingDetailActivity::class.java)
                intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
                startActivity(intent)
            }
        }
    }

    @Composable
    fun FavoritesScreen(onItemClick: (Painting) -> Unit) {
        val favorites = repository.favoritesFlow().collectAsState(initial = emptyList())
        MaterialTheme {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(favorites.value, key = { it.id }) { painting ->
                    PaintingItem(painting, onItemClick)
                }
            }
        }
    }

    @Composable
    fun PaintingItem(painting: Painting, onClick: (Painting) -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(painting) }
                .padding(16.dp)
        ) {
            AsyncImage(
                model = painting.thumbUrl,
                contentDescription = painting.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = painting.artistName,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            )
            Text(
                text = painting.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 2.dp)
            )
            Text(
                text = painting.year,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 2.dp)
            )
        }
    }
}
