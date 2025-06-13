package com.wikiart

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items as pagingItems
import coil.compose.AsyncImage
import com.wikiart.model.Artist
import com.wikiart.model.ArtistCategory
import com.wikiart.model.LayoutType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistsScreen(
    modifier: Modifier = Modifier,
    onArtistClick: (Artist) -> Unit,
    repository: PaintingRepository = PaintingRepository(LocalContext.current)
) {
    var category by remember { mutableStateOf(ArtistCategory.POPULAR) }
    var layoutType by remember { mutableStateOf(LayoutType.COLUMN) }
    var showOptions by remember { mutableStateOf(false) }

    val artists = remember(category) { repository.artistsPagingFlow(category) }
        .collectAsLazyPagingItems()

    MaterialTheme {
        Box(modifier = modifier.fillMaxSize()) {
            Column {
                Button(
                    onClick = { showOptions = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = stringResource(id = category.nameRes()))
                }
                when (layoutType) {
                    LayoutType.LIST -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            pagingItems(artists, key = { it.id }) { artist ->
                                artist?.let { ArtistRow(it, onArtistClick) }
                            }
                            if (artists.loadState.append is LoadState.Loading) {
                                item { LoadingRow() }
                            }
                        }
                    }
                    LayoutType.COLUMN -> {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(artists.itemCount) { index ->
                                artists[index]?.let { ArtistGridItem(it, onArtistClick) }
                            }
                            if (artists.loadState.append is LoadState.Loading) {
                                item { LoadingRow() }
                            }
                        }
                    }
                    LayoutType.SHEET -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(artists.itemCount) { index ->
                                artists[index]?.let { ArtistSheetItem(it, onArtistClick) }
                            }
                            if (artists.loadState.append is LoadState.Loading) {
                                item { LoadingRow() }
                            }
                        }
                    }
                    else -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            pagingItems(artists, key = { it.id }) { artist ->
                                artist?.let { ArtistRow(it, onArtistClick) }
                            }
                            if (artists.loadState.append is LoadState.Loading) {
                                item { LoadingRow() }
                            }
                        }
                    }
                }
            }
            if (showOptions) {
                OptionsBottomSheet(
                    categories = ArtistCategory.values(),
                    selectedCategory = category,
                    layoutType = layoutType,
                    onApply = { cat, layout ->
                        cat?.let { category = it }
                        layoutType = layout
                    },
                    onDismiss = { showOptions = false }
                )
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
private fun ArtistRow(artist: Artist, onClick: (Artist) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(artist) }
            .padding(16.dp)
    ) {
        artist.image?.let { url ->
            coil.compose.AsyncImage(
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

@Composable
private fun ArtistGridItem(artist: Artist, onClick: (Artist) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(artist) },
        horizontalAlignment = Alignment.CenterHorizontally
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
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        val works = artist.totalWorksTitle?.replaceFirstChar { c -> c.uppercaseChar() }
        if (!works.isNullOrBlank()) {
            Text(
                text = works,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        val info = listOfNotNull(artist.nation, artist.year).joinToString(", ")
        if (info.isNotBlank()) {
            Text(
                text = info,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun ArtistSheetItem(artist: Artist, onClick: (Artist) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable { onClick(artist) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        artist.image?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = artist.title,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
