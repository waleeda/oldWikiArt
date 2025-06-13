package com.wikiart

import android.content.Intent
import android.app.ActivityOptions
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.cachedIn
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.content.Context
import androidx.appcompat.widget.PopupMenu
import com.wikiart.model.LayoutType

class SearchFragment : Fragment() {
    private var layoutType: LayoutType = LayoutType.COLUMN
    private lateinit var layoutButton: View
    private lateinit var recyclerView: RecyclerView
    private val adapter = PaintingAdapter(layoutType) { painting, image ->
        val intent = Intent(requireContext(), PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_TITLE, painting.title)
        intent.putExtra(PaintingDetailActivity.EXTRA_IMAGE, painting.detailUrl)
        val options = ActivityOptions.makeSceneTransitionAnimation(
            requireActivity(),
            image,
            image.transitionName
        )
        startActivity(intent, options.toBundle())
    }

    private val repository by lazy { PaintingRepository(requireContext()) }
    private var searchJob: Job? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val input: AutoCompleteTextView = view.findViewById(R.id.searchInput)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        recyclerView = view.findViewById(R.id.resultsRecyclerView)
        layoutButton = view.findViewById(R.id.layoutButton)

        val prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val name = prefs.getString("layout_type", LayoutType.COLUMN.name) ?: LayoutType.COLUMN.name
        layoutType = runCatching { LayoutType.valueOf(name) }.getOrDefault(LayoutType.COLUMN)

        recyclerView.layoutManager = layoutManagerFor(layoutType)
        adapter.layoutType = layoutType
        recyclerView.adapter = adapter
        layoutButton.setOnClickListener { showLayoutMenu(it) }
        swipeRefreshLayout.setOnRefreshListener { adapter.refresh() }
        adapter.addLoadStateListener { loadState ->
            swipeRefreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
        }

        adapter.addLoadStateListener { state ->
            val error = when {
                state.refresh is LoadState.Error -> state.refresh as LoadState.Error
                state.append is LoadState.Error -> state.append as LoadState.Error
                state.prepend is LoadState.Error -> state.prepend as LoadState.Error
                else -> null
            }
            error?.let {
                Snackbar.make(view, R.string.load_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry) { adapter.retry() }
                    .show()
            }
        }

        val suggestions = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line)
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
                    viewLifecycleOwner.lifecycleScope.launch {
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
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            repository.searchPagingFlow(term)
                .cachedIn(viewLifecycleOwner.lifecycleScope)
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
    }

    private fun layoutManagerFor(type: LayoutType): RecyclerView.LayoutManager = when (type) {
        LayoutType.COLUMN -> GridLayoutManager(requireContext(), 2)
        LayoutType.SHEET -> StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        else -> LinearLayoutManager(requireContext())
    }

    private fun showLayoutMenu(anchor: View) {
        PopupMenu(requireContext(), anchor).apply {
            menuInflater.inflate(R.menu.layout_menu, menu)
            setOnMenuItemClickListener { item ->
                val prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
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
