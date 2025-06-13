package com.wikiart

import android.os.Bundle

import com.google.android.material.transition.platform.MaterialFadeThrough

import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    companion object {
        private const val SUPPORT_TRIGGER_VALUE = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nav: BottomNavigationView = findViewById(R.id.bottomNavigation)
        nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_paintings -> {
                    switchFragment(PaintingsFragment())
                    true
                }

                R.id.nav_artists -> {
                    switchFragment(ArtistsFragment())
                    true
                }
                R.id.nav_search -> {
                    switchFragment(SearchFragment())
                    true
                }
                R.id.nav_favorites -> {
                    switchFragment(FavoritesFragment())
                    true
                }
                R.id.nav_support -> {
                    switchFragment(SupportFragment())
                    true
                }
                else -> false
            }

        }
        // Display the Paintings fragment on launch
        if (savedInstanceState == null) {
            nav.selectedItemId = R.id.nav_paintings
        }
    }

    private fun switchFragment(fragment: Fragment) {
        fragment.enterTransition = MaterialFadeThrough()
        fragment.exitTransition = MaterialFadeThrough()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
