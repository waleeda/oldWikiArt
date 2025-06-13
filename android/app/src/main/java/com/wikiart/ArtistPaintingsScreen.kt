package com.wikiart

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.wikiart.model.ArtistPaintingSort
import com.wikiart.model.LayoutType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistPaintingsScreen(
    artistUrl: String,
    modifier: Modifier = Modifier,
    onPaintingClick: (Painting) -> Unit,
    repository: PaintingRepository = PaintingRepository(LocalContext.current)
) {
    var sort by remember { mutableStateOf(ArtistPaintingSort.DATE) }
    var layoutType by remember { mutableStateOf(LayoutType.SHEET) }
    var showOptions by remember { mutableStateOf(false) }

    val paintings = remember(artistUrl, sort) {
        repository.artistPaintingsPagingFlow(artistUrl, sort)
    }.collectAsLazyPagingItems()

    MaterialTheme {
        Box(modifier = modifier.fillMaxSize()) {
            Column {
                SortRow(sort = sort, onSortChange = { sort = it })
                when (layoutType) {
                    LayoutType.LIST -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(
                                count = paintings.itemCount,
                                key = { index -> paintings[index]?.id }
                            ) { index ->
                                paintings[index]?.let { PaintingColumnItem(it, onPaintingClick) }
                            }
                            if (paintings.loadState.append is LoadState.Loading) {
                                item { LoadingRow() }
                            }
                        }
                    }
                    LayoutType.COLUMN -> {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                count = paintings.itemCount,
                                key = { index -> paintings[index]?.id }
                            ) { index ->
                                paintings[index]?.let { PaintingGridItem(it, onPaintingClick) }
                            }
                            if (paintings.loadState.append is LoadState.Loading) {
                                item { LoadingRow() }
                            }
                        }
                    }
                    LayoutType.SHEET -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            gridItems(paintings.itemCount) { index ->
                                paintings[index]?.let { PaintingSheetItem(it, onPaintingClick) }
                            }
                            if (paintings.loadState.append is LoadState.Loading) {
                                item { LoadingRow() }
                            }
                        }
                    }
                    else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(
                            count = paintings.itemCount,
                            key = { index -> paintings[index]?.id }
                        ) { index ->
                            paintings[index]?.let { PaintingColumnItem(it, onPaintingClick) }
                        }
                        if (paintings.loadState.append is LoadState.Loading) {
                            item { LoadingRow() }
                        }
                    }
                    }
                }
            }
            Button(
                onClick = { showOptions = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.change_layout))
            }
            if (showOptions) {
                OptionsBottomSheet(
                    categories = null,
                    selectedCategory = null,
                    layoutType = layoutType,
                    onApply = { _, layout -> layoutType = layout },
                    onDismiss = { showOptions = false }
                )
            }
        }
    }
}

@Composable
private fun SortRow(sort: ArtistPaintingSort, onSortChange: (ArtistPaintingSort) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { onSortChange(ArtistPaintingSort.DATE) }, enabled = sort != ArtistPaintingSort.DATE) {
            Text(text = stringResource(R.string.sort_by_date))
        }
        Button(onClick = { onSortChange(ArtistPaintingSort.NAME) }, enabled = sort != ArtistPaintingSort.NAME) {
            Text(text = stringResource(R.string.sort_by_name))
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
private fun PaintingColumnItem(painting: Painting, onClick: (Painting) -> Unit) {
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
private fun PaintingGridItem(painting: Painting, onClick: (Painting) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(painting) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = painting.thumbUrl,
            contentDescription = painting.title,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = painting.artistName,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = painting.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 2.dp)
        )
        Text(
            text = painting.year,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun PaintingSheetItem(painting: Painting, onClick: (Painting) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable { onClick(painting) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = painting.thumbUrl,
            contentDescription = painting.title,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

