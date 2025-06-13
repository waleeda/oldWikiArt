package com.wikiart

import android.os.Bundle
import android.content.Intent
import android.app.ActivityOptions
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.text.TextWatcher
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.paging.cachedIn
import android.content.Context
import androidx.appcompat.widget.PopupMenu
import com.wikiart.model.LayoutType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private var layoutType: LayoutType = LayoutType.COLUMN
    private lateinit var layoutButton: View
    private lateinit var recyclerView: RecyclerView
    private val adapter = PaintingAdapter(layoutType) { painting ->
        val intent = Intent(this, PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_TITLE, painting.title)
        intent.putExtra(PaintingDetailActivity.EXTRA_IMAGE, painting.detailUrl)
        val options = ActivityOptions.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private val repository = PaintingRepository(this)
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val input: AutoCompleteTextView = findViewById(R.id.searchInput)
        recyclerView = findViewById(R.id.resultsRecyclerView)
        layoutButton = findViewById(R.id.layoutButton)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val name = prefs.getString("layout_type", LayoutType.COLUMN.name) ?: LayoutType.COLUMN.name
        layoutType = runCatching { LayoutType.valueOf(name) }.getOrDefault(LayoutType.COLUMN)

        recyclerView.layoutManager = layoutManagerFor(layoutType)
        adapter.layoutType = layoutType
        recyclerView.adapter = adapter
        layoutButton.setOnClickListener { showLayoutMenu(it) }

        val suggestions = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)
        input.setAdapter(suggestions)

        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(input.text.toString())
                true
            } else {
                false
            }
        }

        input.setOnItemClickListener { _, _, position, _ ->
            val term = suggestions.getItem(position) ?: return@setOnItemClickListener
            performSearch(term)
        }

        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString() ?: return
                if (text.length >= 2) {
                    lifecycleScope.launch {
                        val autos = repository.autoComplete(text)
                        suggestions.clear()
                        suggestions.addAll(autos.map { it.label })
                        suggestions.notifyDataSetChanged()
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun performSearch(term: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            repository.searchPagingFlow(term)
                .cachedIn(lifecycleScope)
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
    }

    private fun layoutManagerFor(type: LayoutType): RecyclerView.LayoutManager = when (type) {
        LayoutType.COLUMN -> GridLayoutManager(this, 2)
        LayoutType.SHEET -> StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        else -> LinearLayoutManager(this)
    }

    private fun showLayoutMenu(anchor: View) {
        PopupMenu(this, anchor).apply {
            menuInflater.inflate(R.menu.layout_menu, menu)
            setOnMenuItemClickListener { item ->
                val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                layoutType = when (item.itemId) {
                    R.id.layout_list -> LayoutType.LIST
                    R.id.layout_grid -> LayoutType.COLUMN
                    R.id.layout_sheet -> LayoutType.SHEET
                    else -> return@setOnMenuItemClickListener false
                }
                prefs.edit().putString("layout_type", layoutType.name).apply()
                adapter.layoutType = layoutType
                adapter.notifyDataSetChanged()
                recyclerView.layoutManager = layoutManagerFor(layoutType)
                true
            }
            show()
        }
    }
}
