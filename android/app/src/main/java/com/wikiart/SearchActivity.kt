package com.wikiart

import android.os.Bundle
import android.content.Intent
import android.app.ActivityOptions
import android.widget.ImageView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ConcatAdapter
import androidx.paging.cachedIn
import android.content.Context
import com.wikiart.model.LayoutType
import com.wikiart.OptionsBottomSheet
import com.wikiart.PaintingCategory
import android.view.Menu
import android.view.MenuItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private var layoutType: LayoutType = LayoutType.COLUMN
    private lateinit var recyclerView: RecyclerView
    private val paintingAdapter = PaintingAdapter(layoutType) { painting, image ->
        val intent = Intent(this, PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_TITLE, painting.title)
        intent.putExtra(PaintingDetailActivity.EXTRA_IMAGE, painting.detailUrl)
        val options = ActivityOptions.makeSceneTransitionAnimation(
            this,
            image,
            image.transitionName
        )
        startActivity(intent, options.toBundle())
    }

    private val artistAdapter = ArtistAdapter { artist ->
        val intent = Intent(this, ArtistDetailActivity::class.java)
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_URL, artist.artistUrl)
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_NAME, artist.title)
        val options = ActivityOptions.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private val headerArtists = HeaderAdapter(R.string.artists)
    private val headerPaintings = HeaderAdapter(R.string.paintings)
    private val concatAdapter = ConcatAdapter(headerArtists, artistAdapter, headerPaintings, paintingAdapter)

    private val repository by lazy { PaintingRepository(this) }
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.resultsRecyclerView)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val name = prefs.getString("layout_type", LayoutType.COLUMN.name) ?: LayoutType.COLUMN.name
        layoutType = runCatching { LayoutType.valueOf(name) }.getOrDefault(LayoutType.COLUMN)

        recyclerView.layoutManager = layoutManagerFor(layoutType)
        paintingAdapter.layoutType = layoutType
        recyclerView.adapter = concatAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_options -> {
                showOptionsSheet()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performSearch(term: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            launch {
                repository.searchArtistsPagingFlow(term)
                    .cachedIn(lifecycleScope)
                    .collectLatest { pagingData ->
                        artistAdapter.submitData(pagingData)
                    }
            }
            launch {
                repository.searchPagingFlow(term)
                    .cachedIn(lifecycleScope)
                    .collectLatest { pagingData ->
                        paintingAdapter.submitData(pagingData)
                    }
            }
        }
    }

    private fun layoutManagerFor(type: LayoutType): RecyclerView.LayoutManager = when (type) {
        LayoutType.COLUMN -> GridLayoutManager(this, 2)
        LayoutType.SHEET -> StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        else -> LinearLayoutManager(this)
    }

    private fun showOptionsSheet() {
        OptionsBottomSheet<PaintingCategory>(null, null, layoutType) { _, layout ->
            if (layout != layoutType) {
                val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                layoutType = layout
                prefs.edit().putString("layout_type", layoutType.name).apply()
                paintingAdapter.layoutType = layoutType
                paintingAdapter.notifyDataSetChanged()
                recyclerView.layoutManager = layoutManagerFor(layoutType)
            }
        }.show(supportFragmentManager, "options")
    }
}
