package com.wikiart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selected by remember { mutableStateOf(NavItem.Paintings) }
            MaterialTheme {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavItem.values().forEach { item ->
                                NavigationBarItem(
                                    icon = { Icon(painterResource(item.icon), null) },
                                    label = { Text(stringResource(item.label)) },
                                    selected = selected == item,
                                    onClick = { selected = item }
                                )
                            }
                        }
                    }
                ) { inner ->
                    when (selected) {
                        NavItem.Paintings -> PaintingsPlaceholderScreen(Modifier.padding(inner))
                        NavItem.Artists -> ArtistsPlaceholderScreen(Modifier.padding(inner))
                        NavItem.Search -> SearchPlaceholderScreen(Modifier.padding(inner))
                        NavItem.Favorites -> FavoritesScreen(
                            onItemClick = { painting ->
                                val intent = Intent(this, PaintingDetailActivity::class.java)
                                intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
                                startActivity(intent)
                            },
                            modifier = Modifier.padding(inner)
                        )
                        NavItem.Support -> SupportScreen(
                            onSendFeedback = {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:wikiartfeedback@icloud.com")
                                }
                                startActivity(intent)
                            },
                            onDonate = {}
                        )
                    }
                }
            }
        }
    }
}

enum class NavItem(val label: Int, val icon: Int) {
    Paintings(R.string.painting_category_popular, R.drawable.ic_gallery),
    Artists(R.string.artists, R.drawable.ic_artists),
    Search(R.string.search, R.drawable.ic_search),
    Favorites(R.string.favorites, android.R.drawable.btn_star_big_on),
    Support(R.string.support, R.drawable.ic_support)
}
