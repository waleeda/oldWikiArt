package com.wikiart

import android.os.Bundle

import android.content.Intent
import android.app.ActivityOptions
import com.google.android.material.transition.platform.MaterialFadeThrough

import androidx.appcompat.app.AppCompatActivity
import com.wikiart.SupportActivity
import com.wikiart.FavoritesActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    companion object {
        private const val SUPPORT_TRIGGER_VALUE = 3
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val random = (1..5).random()
            if (random == SUPPORT_TRIGGER_VALUE) {
                val intent = Intent(this, SupportActivity::class.java)
                val options = ActivityOptions.makeSceneTransitionAnimation(this)
                startActivity(intent, options.toBundle())
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }


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
                R.id.nav_favorites -> {
                    val intent = Intent(this, FavoritesActivity::class.java)
                    val options = ActivityOptions.makeSceneTransitionAnimation(this)
                    startActivity(intent, options.toBundle())
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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
        fragment.enterTransition = MaterialFadeThrough()
        fragment.exitTransition = MaterialFadeThrough()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
