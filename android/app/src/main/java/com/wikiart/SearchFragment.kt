package com.wikiart

import android.content.Intent
import android.app.ActivityOptions
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.cachedIn
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.content.Context
import com.wikiart.model.LayoutType
import com.wikiart.OptionsBottomSheet
import com.wikiart.PaintingCategory
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

class SearchFragment : Fragment() {
    private var layoutType: LayoutType = LayoutType.COLUMN
    private lateinit var recyclerView: RecyclerView
    private val paintingAdapter = PaintingAdapter(layoutType) { painting, image ->
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

    private val artistAdapter = ArtistAdapter { artist ->
        val intent = Intent(requireContext(), ArtistDetailActivity::class.java)
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_URL, artist.artistUrl)
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_NAME, artist.title)
        val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity())
        startActivity(intent, options.toBundle())
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private val headerArtists = HeaderAdapter(R.string.artists)
    private val headerPaintings = HeaderAdapter(R.string.paintings)
    private val concatAdapter = ConcatAdapter(headerArtists, artistAdapter, headerPaintings, paintingAdapter)

    private val repository by lazy { PaintingRepository(requireContext()) }
    private var searchJob: Job? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        recyclerView = view.findViewById(R.id.resultsRecyclerView)

        val prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val name = prefs.getString("layout_type", LayoutType.COLUMN.name) ?: LayoutType.COLUMN.name
        layoutType = runCatching { LayoutType.valueOf(name) }.getOrDefault(LayoutType.COLUMN)

        recyclerView.layoutManager = layoutManagerFor(layoutType)
        paintingAdapter.layoutType = layoutType
        recyclerView.adapter = concatAdapter
        swipeRefreshLayout.setOnRefreshListener {
            paintingAdapter.refresh()
            artistAdapter.refresh()
        }
        paintingAdapter.addLoadStateListener { loadState ->
            swipeRefreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
        }
        artistAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.Error) {
                Snackbar.make(view, R.string.load_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry) { artistAdapter.retry() }
                    .show()
            }
        }

        paintingAdapter.addLoadStateListener { state ->
            val error = when {
                state.refresh is LoadState.Error -> state.refresh as LoadState.Error
                state.append is LoadState.Error -> state.append as LoadState.Error
                state.prepend is LoadState.Error -> state.prepend as LoadState.Error
                else -> null
            }
            error?.let {
                Snackbar.make(view, R.string.load_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry) { paintingAdapter.retry() }
                    .show()
            }
        }
    }

    private fun performSearch(term: String) {
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            launch {
                repository.searchArtistsPagingFlow(term)
                    .cachedIn(viewLifecycleOwner.lifecycleScope)
                    .collectLatest { pagingData ->
                        artistAdapter.submitData(pagingData)
                    }
            }
            launch {
                repository.searchPagingFlow(term)
                    .cachedIn(viewLifecycleOwner.lifecycleScope)
                    .collectLatest { pagingData ->
                        paintingAdapter.submitData(pagingData)
                    }
            }
        }
    }

    private fun layoutManagerFor(type: LayoutType): RecyclerView.LayoutManager = when (type) {
        LayoutType.COLUMN -> GridLayoutManager(requireContext(), 2)
        LayoutType.SHEET -> StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        else -> LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
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

    private fun showOptionsSheet() {
        OptionsBottomSheet<PaintingCategory>(null, null, layoutType) { _, layout ->
            if (layout != layoutType) {
                val prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                layoutType = layout
                prefs.edit().putString("layout_type", layoutType.name).apply()
                paintingAdapter.layoutType = layoutType
                paintingAdapter.notifyDataSetChanged()
                recyclerView.layoutManager = layoutManagerFor(layoutType)
            }
        }.show(parentFragmentManager, "options")
    }
}
