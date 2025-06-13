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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.content.Context
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wikiart.model.LayoutType
import com.wikiart.model.PaintingSection
import com.wikiart.OptionsBottomSheet
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PaintingsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PaintingAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var layoutType: LayoutType = LayoutType.COLUMN
    private var category: PaintingCategory = PaintingCategory.FEATURED

    private val itemClick: (Painting, ImageView) -> Unit = { painting, image ->
        val intent = Intent(requireContext(), PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
        val options = ActivityOptions.makeSceneTransitionAnimation(
            requireActivity(),
            image,
            image.transitionName
        )
        startActivity(intent, options.toBundle())
    }

    private val repository by lazy { PaintingRepository(requireContext()) }
    private var pagingJob: Job? = null
    private var currentSectionId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_paintings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val name = prefs.getString("layout_type", LayoutType.COLUMN.name) ?: LayoutType.COLUMN.name
        layoutType = runCatching { LayoutType.valueOf(name) }.getOrDefault(LayoutType.COLUMN)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        recyclerView = view.findViewById(R.id.paintingRecyclerView)
        adapter = PaintingAdapter(layoutType, itemClick)
        recyclerView.layoutManager = layoutManagerFor(layoutType)
        recyclerView.adapter = adapter
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

    private fun layoutManagerFor(type: LayoutType): RecyclerView.LayoutManager = when (type) {
        LayoutType.COLUMN -> StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
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
        OptionsBottomSheet(PaintingCategory.values(), category, layoutType) { cat, layout ->
            val prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            if (layout != layoutType) {
                layoutType = layout
                prefs.edit().putString("layout_type", layoutType.name).apply()
                adapter.layoutType = layoutType
                adapter.notifyDataSetChanged()
                recyclerView.layoutManager = layoutManagerFor(layoutType)
            }
            cat?.let { selected ->
                if (selected != category) {
                    category = selected
                    if (selected.hasSections()) {
                        lifecycleScope.launch {
                            val sections = repository.sections(selected)
                            if (sections.isNotEmpty()) {
                                showSectionDialog(selected, sections)
                            }
                        }
                    } else {
                        currentSectionId = null
                        loadCategory(selected)
                    }
                }
            }
        }.show(parentFragmentManager, "options")
    }

    private fun loadCategory(category: PaintingCategory, sectionId: String? = null) {
        // Fragment's view might be destroyed (e.g. after configuration change)
        // when a category is selected. Guard against accessing viewLifecycleOwner
        // in that case to avoid crashes.
        val owner = viewLifecycleOwnerLiveData.value ?: return

        pagingJob?.cancel()
        pagingJob = owner.lifecycleScope.launch {
            repository.pagingFlow(category, sectionId)
                .cachedIn(owner.lifecycleScope)
                .collect { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
    }

    private fun showSectionDialog(category: PaintingCategory, sections: List<PaintingSection>) {
        val names = sections.map { it.titleForLanguage("en") as CharSequence }.toTypedArray()
        val categoryNames = resources.getStringArray(R.array.painting_category_names)
        val title = categoryNames[PaintingCategory.values().indexOf(category)]
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setItems(names) { _, which ->
                currentSectionId = sections[which].id.oid
                loadCategory(category, currentSectionId)
            }
            .show()
    }
}
