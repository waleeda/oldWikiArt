package com.wikiart

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wikiart.data.FavoritesRepository

@Composable
fun FavoritesScreen(
    onItemClick: (Painting) -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    val repository = remember(context) { FavoritesRepository(context) }
    val favorites = repository.favoritesFlow().collectAsState(initial = emptyList())
    MaterialTheme {
        LazyColumn(modifier = modifier.fillMaxSize()) {
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
