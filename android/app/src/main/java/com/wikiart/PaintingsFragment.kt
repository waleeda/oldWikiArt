package com.wikiart

import android.content.Intent
import android.app.ActivityOptions
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.cachedIn
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.content.Context
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wikiart.model.LayoutType
import com.wikiart.model.PaintingSection
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PaintingsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PaintingAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var layoutType: LayoutType = LayoutType.COLUMN

    private val itemClick: (Painting) -> Unit = { painting ->
        val intent = Intent(requireContext(), PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
        val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity())
        startActivity(intent, options.toBundle())
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private val repository = PaintingRepository()
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

        val spinner: Spinner = view.findViewById(R.id.categorySpinner)
        val categories = PaintingCategory.values()
        val categoryNames = resources.getStringArray(R.array.painting_category_names)
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categoryNames)
        spinner.setSelection(categories.indexOf(PaintingCategory.FEATURED))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val category = categories[position]
                if (category.hasSections()) {
                    lifecycleScope.launch {
                        val sections = repository.sections(category)
                        if (sections.isNotEmpty()) {
                            showSectionDialog(category, sections)
                        }
                    }
                } else {
                    currentSectionId = null
                    loadCategory(category)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        loadCategory(PaintingCategory.FEATURED)
    }

    private fun layoutManagerFor(type: LayoutType): RecyclerView.LayoutManager = when (type) {
        LayoutType.COLUMN -> GridLayoutManager(requireContext(), 2)
        LayoutType.SHEET -> StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        else -> LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        layoutType = when (item.itemId) {
            R.id.layout_list -> LayoutType.LIST
            R.id.layout_grid -> LayoutType.COLUMN
            R.id.layout_sheet -> LayoutType.SHEET
            else -> return super.onOptionsItemSelected(item)
        }
        prefs.edit().putString("layout_type", layoutType.name).apply()
        adapter.layoutType = layoutType
        adapter.notifyDataSetChanged()
        recyclerView.layoutManager = layoutManagerFor(layoutType)
        return true
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
