package com.example.wikiart

import android.content.Intent
import android.net.Uri
import androidx.navigation.findNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeepLinkNavigationTest {

    @Test
    fun opensPaintingLink() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wikiart.org/en/artist/sample-painting"))
        ActivityScenario.launch<MainActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                val navController = activity.findNavController(R.id.nav_host_fragment_activity_main)
                assertEquals(R.id.paintingDetailFragment, navController.currentDestination?.id)
            }
        }
    }

    @Test
    fun opensArtistLink() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wikiart.org/en/sample-artist"))
        ActivityScenario.launch<MainActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                val navController = activity.findNavController(R.id.nav_host_fragment_activity_main)
                assertEquals(R.id.artistDetailFragment, navController.currentDestination?.id)
            }
        }
    }
}
