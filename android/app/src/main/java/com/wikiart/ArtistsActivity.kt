package com.wikiart

import android.content.Intent
import android.app.ActivityOptions
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.wikiart.CategorySpinnerAdapter
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
        val options = ActivityOptions.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private val repository by lazy { PaintingRepository(this) }
    private var pagingJob: Job? = null
    private var currentSection: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artists)

        val recycler: RecyclerView = findViewById(R.id.artistRecyclerView)
        val columns = if (resources.getBoolean(R.bool.isTablet)) 4 else 2
        recycler.layoutManager = GridLayoutManager(this, columns)
        recycler.adapter = adapter

        val spinner: Spinner = findViewById(R.id.artistCategorySpinner)
        val categories = ArtistCategory.values()
        val spinnerAdapter = CategorySpinnerAdapter(this, categories)
        spinner.adapter = spinnerAdapter
        spinner.setSelection(categories.indexOf(ArtistCategory.POPULAR))

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
        val categoryNames = resources.getStringArray(R.array.artist_category_names)
        val title = categoryNames[ArtistCategory.values().indexOf(category)]
        AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(names) { _, which ->
                currentSection = sections[which].url
                loadCategory(category)
            }
            .show()
    }
}
