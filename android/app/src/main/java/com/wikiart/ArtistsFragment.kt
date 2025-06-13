package com.wikiart

import android.content.Intent
import android.app.ActivityOptions
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.cachedIn
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wikiart.model.ArtistCategory
import com.wikiart.model.ArtistSection
import com.wikiart.OptionsBottomSheet
import com.wikiart.model.LayoutType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ArtistsFragment : Fragment() {
    private val adapter = ArtistAdapter { artist ->
        val intent = Intent(requireContext(), ArtistDetailActivity::class.java)
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_URL, artist.artistUrl)
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_NAME, artist.title)
        val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity())
        startActivity(intent, options.toBundle())
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private val repository by lazy { PaintingRepository(requireContext()) }
    private var pagingJob: Job? = null
    private var currentSection: String? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var category: ArtistCategory = ArtistCategory.POPULAR

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_artists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        val recycler: RecyclerView = view.findViewById(R.id.artistRecyclerView)
        val columns = if (resources.getBoolean(R.bool.isTablet)) 4 else 2
        recycler.layoutManager = GridLayoutManager(requireContext(), columns)
        recycler.adapter = adapter
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

        loadCategory(category)
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
        OptionsBottomSheet(ArtistCategory.values(), category, LayoutType.COLUMN) { cat, _ ->
            cat?.let { selected ->
                if (selected != category) {
                    category = selected
                    if (selected.hasSections()) {
                        lifecycleScope.launch {
                            val sections = repository.artistSections(selected)
                            if (sections.isNotEmpty()) {
                                showSectionDialog(selected, sections)
                            }
                        }
                    } else {
                        currentSection = null
                        loadCategory(selected)
                    }
                }
            }
        }.show(parentFragmentManager, "options")
    }

    private fun loadCategory(category: ArtistCategory) {
        pagingJob?.cancel()
        pagingJob = viewLifecycleOwner.lifecycleScope.launch {
            repository.artistsPagingFlow(category, currentSection)
                .cachedIn(viewLifecycleOwner.lifecycleScope)
                .collect { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
    }

    private fun showSectionDialog(category: ArtistCategory, sections: List<ArtistSection>) {
        val names = sections.map { it.title }.toTypedArray()
        val categoryNames = resources.getStringArray(R.array.artist_category_names)
        val title = categoryNames[ArtistCategory.values().indexOf(category)]
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setItems(names) { _, which ->
                currentSection = sections[which].url
                loadCategory(category)
            }
            .show()
    }
}
