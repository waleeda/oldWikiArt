package com.wikiart

import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.paging.cachedIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val adapter = PaintingAdapter { painting ->
        val intent = Intent(this, PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
        startActivity(intent)
    }

    private val repository = PaintingRepository()
    private var pagingJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.paintingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val spinner: Spinner = findViewById(R.id.categorySpinner)
        val categories = PaintingCategory.values()
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                loadCategory(categories[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Load default category
        loadCategory(PaintingCategory.FEATURED)
    }

    private fun loadCategory(category: PaintingCategory) {
        pagingJob?.cancel()
        pagingJob = lifecycleScope.launch {
            repository.pagingFlow(category)
                .cachedIn(lifecycleScope)
                .collect { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorites -> {
                startActivity(Intent(this, FavoritesActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
