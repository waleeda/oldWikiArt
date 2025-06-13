package com.wikiart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.res.painterResource
import com.wikiart.data.FavoritesRepository
import kotlinx.coroutines.launch

@Composable
fun PaintingDetailScreen(
    painting: Painting,
    onRelatedClick: (Painting) -> Unit,
    onArtistClick: (String, String) -> Unit,
    onShare: (Painting) -> Unit,
    onBuy: (Painting) -> Unit,
    repository: PaintingRepository = PaintingRepository(LocalContext.current),
    favoritesRepository: FavoritesRepository = FavoritesRepository(LocalContext.current),
) {
    var details by remember { mutableStateOf<PaintingDetails?>(null) }
    var related by remember { mutableStateOf<List<Painting>>(emptyList()) }
    var isFavorite by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(painting.id) {
        isFavorite = favoritesRepository.isFavorite(painting.id)
        details = repository.getPaintingDetails(painting.id)
        related = repository.getRelatedPaintings(painting.paintingUrl)
    }

    MaterialTheme {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            AsyncImage(
                model = painting.detailUrl,
                contentDescription = painting.title,
                modifier = Modifier.fillMaxWidth()
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(painting.title, style = MaterialTheme.typography.titleLarge)
                if (painting.year.isNotBlank()) {
                    Text(stringResource(R.string.year, painting.year))
                }
                if (painting.width > 0 && painting.height > 0) {
                    Text(stringResource(R.string.dimensions, painting.width, painting.height))
                }
                details?.let { d ->
                    d.location?.takeIf { it.isNotBlank() }?.let {
                        DetailField(stringResource(R.string.location_label), it)
                    }
                    d.period?.takeIf { it.isNotBlank() }?.let {
                        DetailField(stringResource(R.string.period_label), it)
                    }
                    d.series?.takeIf { it.isNotBlank() }?.let {
                        DetailField(stringResource(R.string.series_label), it)
                    }
                    d.genres?.takeIf { it.isNotEmpty() }?.let {
                        DetailField(stringResource(R.string.genres_label), it.joinToString(", "))
                    }
                    d.styles?.takeIf { it.isNotEmpty() }?.let {
                        DetailField(stringResource(R.string.styles_label), it.joinToString(", "))
                    }
                    d.media?.takeIf { it.isNotEmpty() }?.let {
                        DetailField(stringResource(R.string.media_label), it.joinToString(", "))
                    }
                    d.galleries?.takeIf { it.isNotEmpty() }?.let {
                        DetailField(stringResource(R.string.galleries_label), it.joinToString(", "))
                    }
                    d.tags?.takeIf { it.isNotEmpty() }?.let {
                        DetailField(stringResource(R.string.tags_label), it.joinToString(", "))
                    }
                    d.description?.takeIf { it.isNotBlank() }?.let {
                        DetailField(stringResource(R.string.description_label), it)
                    }
                }
                Text(
                    text = painting.artistName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable { painting.artistUrl?.let { onArtistClick(it, painting.artistName) } }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    IconToggleButton(
                        checked = isFavorite,
                        onCheckedChange = { checked ->
                            isFavorite = checked
                            scope.launch {
                                if (checked) favoritesRepository.addFavorite(painting)
                                else favoritesRepository.removeFavorite(painting)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
                            ),
                            contentDescription = if (isFavorite) stringResource(R.string.remove_favorite) else stringResource(R.string.add_favorite)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { onShare(painting) }) { Text(stringResource(R.string.share)) }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { onBuy(painting) }) { Text(stringResource(R.string.buy)) }
                }
                if (related.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.related_paintings),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    LazyRow {
                        items(related, key = { it.id }) { item ->
                            RelatedPaintingItem(item, onRelatedClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Row(modifier = Modifier.padding(top = 2.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.width(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun RelatedPaintingItem(painting: Painting, onClick: (Painting) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(160.dp)
            .padding(8.dp)
            .clickable { onClick(painting) }
    ) {
        AsyncImage(
            model = painting.thumbUrl,
            contentDescription = painting.title,
            modifier = Modifier.fillMaxWidth()
        )
        Text(painting.artistName, style = MaterialTheme.typography.labelSmall)
        Text(painting.title, style = MaterialTheme.typography.titleSmall)
        Text(painting.year, style = MaterialTheme.typography.labelSmall)
    }
}

