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

class MainActivity : AppCompatActivity() {
    private val adapter = PaintingAdapter { painting ->
        val intent = Intent(this, PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
        startActivity(intent)
    }

    private val repository = PaintingRepository()
    private var pagingJob: Job? = null
    private var currentSectionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                            showSectionDialog(category, sections)
                        }
                    }
                } else {
                    currentSectionId = null
                    loadCategory(category)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Load default category
        loadCategory(PaintingCategory.FEATURED)
    }

    private fun loadCategory(category: PaintingCategory, sectionId: String? = null) {
        pagingJob?.cancel()
        pagingJob = lifecycleScope.launch {
            repository.pagingFlow(category, sectionId)
                .cachedIn(lifecycleScope)
                .collect { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
    }

    private fun showSectionDialog(category: PaintingCategory, sections: List<PaintingSection>) {
        val names = sections.map { it.titleForLanguage("en") }.toTypedArray()
        val categoryNames = resources.getStringArray(R.array.painting_category_names)
        val title = categoryNames[categories.indexOf(category)]
        AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(names) { _, which ->
                currentSectionId = sections[which].id.oid
                loadCategory(category, currentSectionId)
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                 true
            }
            R.id.action_favorites -> {
                startActivity(Intent(this, FavoritesActivity::class.java))
                true
            }
            R.id.action_support -> {
                startActivity(Intent(this, SupportActivity::class.java))
                true
            }
            R.id.action_artists -> {
                startActivity(Intent(this, ArtistsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
