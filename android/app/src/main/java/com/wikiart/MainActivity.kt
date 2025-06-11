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
