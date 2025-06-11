package com.wikiart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.cachedIn
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wikiart.model.ArtistCategory
import com.wikiart.model.ArtistSection
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArtistsActivity : AppCompatActivity() {
    private val adapter = ArtistAdapter { artist ->
        val intent = Intent(this, ArtistDetailActivity::class.java)
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_URL, artist.artistUrl)
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_NAME, artist.title)
        startActivity(intent)
    }

    private val repository = PaintingRepository()
    private var pagingJob: Job? = null
    private var currentSection: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artists)

        val recycler: RecyclerView = findViewById(R.id.artistRecyclerView)
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = adapter

        val spinner: Spinner = findViewById(R.id.artistCategorySpinner)
        val categories = ArtistCategory.values()
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val category = categories[position]
                if (category.hasSections()) {
                    lifecycleScope.launch {
                        val sections = repository.artistSections(category)
                        if (sections.isNotEmpty()) {
                            showSectionDialog(category, sections)
                        }
                    }
                } else {
                    currentSection = null
                    loadCategory(category)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        loadCategory(ArtistCategory.POPULAR)
    }

    private fun loadCategory(category: ArtistCategory) {
        pagingJob?.cancel()
        pagingJob = lifecycleScope.launch {
            repository.artistsPagingFlow(category, currentSection)
                .cachedIn(lifecycleScope)
                .collect { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
    }

    private fun showSectionDialog(category: ArtistCategory, sections: List<ArtistSection>) {
        val names = sections.map { it.title }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle(category.toString())
            .setItems(names) { _, which ->
                currentSection = sections[which].url
                loadCategory(category)
            }
            .show()
    }
}
