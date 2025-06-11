package com.wikiart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PaintingsFragment())
                .commit()
        }

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
                R.id.nav_support -> {
                    switchFragment(SupportFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
