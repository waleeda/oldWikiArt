package com.wikiart

import android.os.Bundle

import android.content.Intent

import android.view.Menu
import android.view.MenuItem

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import java.util.Locale


import androidx.appcompat.app.AppCompatActivity

import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.paging.cachedIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.wikiart.model.PaintingSection
import com.wikiart.SupportActivity
import com.wikiart.FavoritesActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics

import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    companion object {
        private const val SUPPORT_TRIGGER_VALUE = 3
    }
    private val adapter = PaintingAdapter { painting ->
        val intent = Intent(this, PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
        startActivity(intent)
    }

    private val repository = PaintingRepository()
    private var pagingJob: Job? = null
    private var currentSectionId: String? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val random = (1..5).random()
            if (random == SUPPORT_TRIGGER_VALUE) {
                startActivity(Intent(this, SupportActivity::class.java))
            }
        }


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PaintingsFragment())
                .commit()
}
        val deviceLanguage = Locale.getDefault().language

        val recyclerView: RecyclerView = findViewById(R.id.paintingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val spinner: Spinner = findViewById(R.id.categorySpinner)
        val categories = PaintingCategory.values()
        val categoryNames = resources.getStringArray(R.array.painting_category_names)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryNames)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val category = categories[position]
                if (category.hasSections()) {
                    lifecycleScope.launch {
                        val sections = repository.sections(category)
                        if (sections.isNotEmpty()) {
                            showSectionDialog(category, sections, deviceLanguage)
                        }
                    }
                } else {
                    currentSectionId = null
                    loadCategory(category)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}

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
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.nav_support -> {
                    switchFragment(SupportFragment())
                    true
                }
                else -> false

        }
    }

    private fun showSectionDialog(category: PaintingCategory, sections: List<PaintingSection>, language: String) {
        val names = sections.map { it.titleForLanguage(language) }.toTypedArray()
        val categoryNames = resources.getStringArray(R.array.painting_category_names)
        val title = categoryNames[categories.indexOf(category)]
        AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(names) { _, which ->
                currentSectionId = sections[which].id.oid
                loadCategory(category, currentSectionId)

            }
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
