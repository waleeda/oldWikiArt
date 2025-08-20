package com.example.wikiart.ui.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wikiart.R
import com.example.wikiart.databinding.FragmentArtistsBinding
import com.example.wikiart.model.ArtistCategory

/**
 * Fragment displaying a paginated list of artists.
 */
class ArtistsFragment : Fragment() {

    private var _binding: FragmentArtistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArtistListViewModel by viewModels()
    private lateinit var adapter: ArtistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArtistAdapter(viewModel.layout)
        binding.artistRecyclerView.adapter = adapter
        binding.artistRecyclerView.layoutManager = layoutManagerFor(viewModel.layout)

        val categories = ArtistCategory.values()
        val titles = categories.map { getString(it.titleRes) }
        binding.categorySelector.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, titles)
        binding.categorySelector.setSelection(categories.indexOf(viewModel.category))
        binding.categorySelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = categories[position]
                if (selected != viewModel.category) {
                    viewModel.setCategory(selected)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.categoryButton.setOnClickListener { openOptions() }

        viewModel.artists.observe(viewLifecycleOwner) { adapter.submitList(it) }
        viewModel.loading.observe(viewLifecycleOwner) { binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE }
        viewModel.error.observe(viewLifecycleOwner) { err ->
            err?.let { Toast.makeText(requireContext(), it.localizedMessage ?: "Load failed", Toast.LENGTH_SHORT).show() }
        }

        viewModel.loadNext()

        binding.artistRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val manager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisible = manager.findLastVisibleItemPosition()
                if (lastVisible >= adapter.itemCount - 5) {
                    viewModel.loadNext()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_painting_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_options) {
            openOptions()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun openOptions() {
        ArtistOptionsBottomSheetFragment.newInstance(viewModel.category, viewModel.layout).apply {
            setListener(object : ArtistOptionsBottomSheetFragment.Listener {
                override fun onOptionsSelected(category: ArtistCategory, layout: ArtistAdapter.Layout) {
                    if (viewModel.category != category) {
                        viewModel.setCategory(category)
                    }
                    if (viewModel.layout != layout) {
                        viewModel.setLayout(layout)
                        adapter.setLayout(layout)
                        binding.artistRecyclerView.layoutManager = layoutManagerFor(layout)
                        binding.artistRecyclerView.scrollToPosition(0)
                    }
                }
            })
        }.show(parentFragmentManager, "artist_options")
    }

    private fun layoutManagerFor(layout: ArtistAdapter.Layout): RecyclerView.LayoutManager = when(layout) {
        ArtistAdapter.Layout.LIST -> LinearLayoutManager(requireContext())
        ArtistAdapter.Layout.GRID -> GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
