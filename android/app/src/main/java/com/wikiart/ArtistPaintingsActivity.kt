package com.wikiart

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.paging.cachedIn
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.view.Menu
import android.view.MenuItem
import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.paging.LoadState
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wikiart.model.LayoutType
import com.wikiart.model.ArtistPaintingSort
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArtistPaintingsActivity : AppCompatActivity() {
    private var layoutType: LayoutType = LayoutType.SHEET
    private var sort: ArtistPaintingSort = ArtistPaintingSort.DATE
    private val adapter = PaintingAdapter(layoutType) { painting ->
        val intent = Intent(this, PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
        val options = ActivityOptions.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private val repository = PaintingRepository(this)
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var pagingJob: Job? = null

    private lateinit var artistUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_paintings)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val name = prefs.getString("layout_type", LayoutType.SHEET.name) ?: LayoutType.SHEET.name
        layoutType = runCatching { LayoutType.valueOf(name) }.getOrDefault(LayoutType.SHEET)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        val recyclerView: RecyclerView = findViewById(R.id.allPaintingsRecyclerView)
        recyclerView.layoutManager = layoutManagerFor(layoutType)
        recyclerView.adapter = adapter

        val sortSpinner: Spinner = findViewById(R.id.sortSpinner)
        val sortNames = resources.getStringArray(R.array.artist_painting_sort_names)
        sortSpinner.adapter = ArrayAdapter(this, R.layout.spinner_sort_item, sortNames)
        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sort = ArtistPaintingSort.values()[position]
                loadPaintings()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        swipeRefreshLayout.setOnRefreshListener { adapter.refresh() }
        adapter.addLoadStateListener { loadState ->
            swipeRefreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
        }

        artistUrl = intent.getStringExtra(EXTRA_ARTIST_URL) ?: return

        loadPaintings()
    }

    private fun layoutManagerFor(type: LayoutType): RecyclerView.LayoutManager = when (type) {
        LayoutType.COLUMN -> GridLayoutManager(this, 2)
        LayoutType.SHEET -> StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        else -> LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.layout_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        layoutType = when (item.itemId) {
            R.id.layout_list -> LayoutType.LIST
            R.id.layout_grid -> LayoutType.COLUMN
            R.id.layout_sheet -> LayoutType.SHEET
            else -> return super.onOptionsItemSelected(item)
        }
        prefs.edit().putString("layout_type", layoutType.name).apply()
        adapter.layoutType = layoutType
        adapter.notifyDataSetChanged()
        findViewById<RecyclerView>(R.id.allPaintingsRecyclerView).layoutManager = layoutManagerFor(layoutType)
        return true
    }

    private fun loadPaintings() {
        pagingJob?.cancel()
        pagingJob = lifecycleScope.launch {
            repository.artistPaintingsPagingFlow(artistUrl, sort)
                .cachedIn(lifecycleScope)
                .collect { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
    }

    companion object {
        const val EXTRA_ARTIST_URL = "extra_artist_url"
    }
}
