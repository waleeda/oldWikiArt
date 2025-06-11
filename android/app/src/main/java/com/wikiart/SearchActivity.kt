package com.wikiart

import android.os.Bundle
import android.content.Intent
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.text.TextWatcher
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.paging.cachedIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private val paintingAdapter = PaintingAdapter { painting ->
        val intent = Intent(this, PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_TITLE, painting.title)
        intent.putExtra(PaintingDetailActivity.EXTRA_IMAGE, painting.image)
        startActivity(intent)
    }

    private val artistAdapter = ArtistAdapter { artist ->
        val intent = Intent(this, ArtistDetailActivity::class.java)
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST, artist)
        startActivity(intent)
    }

    private val repository = PaintingRepository()
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val input: AutoCompleteTextView = findViewById(R.id.searchInput)
        val artistRecyclerView: RecyclerView = findViewById(R.id.artistResultsRecyclerView)
        val paintingRecyclerView: RecyclerView = findViewById(R.id.paintingResultsRecyclerView)

        artistRecyclerView.layoutManager = LinearLayoutManager(this)
        artistRecyclerView.adapter = artistAdapter

        paintingRecyclerView.layoutManager = LinearLayoutManager(this)
        paintingRecyclerView.adapter = paintingAdapter

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
}
