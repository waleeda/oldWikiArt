package com.wikiart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.wikiart.model.Artist

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onPaintingClick: (Painting) -> Unit,
    onArtistClick: (Artist) -> Unit,
    repository: PaintingRepository = PaintingRepository(LocalContext.current)
) {
    var query by remember { mutableStateOf("") }
    val artistItems = if (query.isNotBlank())
        repository.searchArtistsPagingFlow(query).collectAsLazyPagingItems() else null
    val paintingItems = if (query.isNotBlank())
        repository.searchPagingFlow(query).collectAsLazyPagingItems() else null

    MaterialTheme {
        Column(modifier = modifier.fillMaxSize()) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                placeholder = { Text(stringResource(R.string.search)) }
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                artistItems?.let { artists ->
                    if (artists.itemCount > 0) {
                        item {
                            Text(
                                text = stringResource(R.string.artists),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    items(artists.itemCount) { index ->
                        artists[index]?.let { artist ->
                            ArtistRow(artist, onArtistClick)
                        }
                    }
                    if (artists.loadState.append is LoadState.Loading) {
                        item { LoadingRow() }
                    }
                }
                paintingItems?.let { paintings ->
                    if (paintings.itemCount > 0) {
                        item {
                            Text(
                                text = stringResource(R.string.paintings),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    items(paintings.itemCount) { index ->
                        paintings[index]?.let { painting ->
                            PaintingRow(painting, onPaintingClick)
                        }
                    }
                    if (paintings.loadState.append is LoadState.Loading) {
                        item { LoadingRow() }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) { CircularProgressIndicator() }
}

@Composable
private fun PaintingRow(painting: Painting, onClick: (Painting) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(painting) }
            .padding(16.dp)
    ) {
        AsyncImage(
            model = painting.thumbUrl,
            contentDescription = painting.title,
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

@Composable
private fun ArtistRow(artist: Artist, onClick: (Artist) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(artist) }
            .padding(16.dp)
    ) {
        artist.image?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = artist.title,
                modifier = Modifier.fillMaxWidth()
            )
        }
        artist.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            )
        }
        val works = artist.totalWorksTitle?.replaceFirstChar { c -> c.uppercaseChar() }
        if (!works.isNullOrBlank()) {
            Text(
                text = works,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 2.dp)
            )
        }
        val info = listOfNotNull(artist.nation, artist.year).joinToString(", ")
        if (info.isNotBlank()) {
            Text(
                text = info,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 2.dp)
            )
        }
    }
}
