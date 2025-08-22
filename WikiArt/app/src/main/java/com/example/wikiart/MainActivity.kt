package com.example.wikiart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wikiart.databinding.ActivityMainBinding
import com.example.wikiart.ui.artists.ArtistDetailFragment
import com.example.wikiart.ui.paintings.PaintingDetailFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_paintings,
                R.id.navigation_artists,
                R.id.navigation_search,
                R.id.navigation_notifications,
                R.id.navigation_support
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        val data: Uri = intent?.data ?: return
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val request = parseDeepLink(data)
        if (request != null) {
            navController.navigate(request.destinationId, request.args)
        } else {
            navController.navigate(R.id.navigation_paintings)
        }
    }

    data class NavigationRequest(val destinationId: Int, val args: Bundle)

    companion object {
        internal fun parseDeepLink(data: Uri): NavigationRequest? {
            if (data.scheme != "https" || data.host != "www.wikiart.org") return null
            val segments = data.pathSegments
            return when {
                segments.size == 3 && segments[1] == "paintings" -> {
                    val id = segments[2]
                    val bundle = Bundle().apply {
                        putString(PaintingDetailFragment.ARG_PAINTING_ID, id)
                    }
                    NavigationRequest(R.id.paintingDetailFragment, bundle)
                }
                segments.size == 3 && segments[1] == "artists" -> {
                    val path = "/${segments[0]}/${segments[1]}/${segments[2]}"
                    val bundle = Bundle().apply {
                        putString(ArtistDetailFragment.ARG_ARTIST_PATH, path)
                    }
                    NavigationRequest(R.id.artistDetailFragment, bundle)
                }
                else -> null
            }
        }
    }
}
