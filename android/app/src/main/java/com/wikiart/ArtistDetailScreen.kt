package com.wikiart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ArtistDetailScreen(
    artistUrl: String,
    artistName: String,
    onPaintingClick: (Painting) -> Unit,
    onSeeAll: () -> Unit,
    repository: PaintingRepository = PaintingRepository(LocalContext.current)
) {
    var details by remember { mutableStateOf<ArtistDetails?>(null) }
    var paintings by remember { mutableStateOf<List<Painting>>(emptyList()) }

    LaunchedEffect(artistUrl) {
        details = repository.getArtistDetails(artistUrl)
        paintings = repository.getFamousPaintings(artistUrl)
    }

    MaterialTheme {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            details?.image?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = details?.artistName ?: artistName,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = details?.artistName ?: artistName,
                    style = MaterialTheme.typography.titleLarge
                )
                details?.biography?.takeIf { it.isNotBlank() }?.let {
                    DetailField(stringResource(R.string.biography_label), it)
                }
                details?.gender?.takeIf { it.isNotBlank() }?.let {
                    DetailField(stringResource(R.string.gender_label), it)
                }
                details?.series?.takeIf { it.isNotBlank() }?.let {
                    DetailField(stringResource(R.string.series_label), it)
                }
                details?.themes?.takeIf { it.isNotBlank() }?.let {
                    DetailField(stringResource(R.string.themes_label), it)
                }
                details?.periods?.takeIf { it.isNotBlank() }?.let {
                    DetailField(stringResource(R.string.periods_label), it)
                }
                details?.birth?.takeIf { it.isNotBlank() }?.let {
                    DetailField(stringResource(R.string.birth_label), it)
                }
                details?.death?.takeIf { it.isNotBlank() }?.let {
                    DetailField(stringResource(R.string.death_label), it)
                }
                if (paintings.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.famous_works),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    LazyRow {
                        items(paintings, key = { it.id }) { painting ->
                            RelatedPaintingItem(painting, onPaintingClick)
                        }
                    }
                    Button(
                        onClick = onSeeAll,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.see_all_paintings))
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
        Text(painting.title, style = MaterialTheme.typography.titleSmall)
        Text(painting.year, style = MaterialTheme.typography.labelSmall)
    }
}
