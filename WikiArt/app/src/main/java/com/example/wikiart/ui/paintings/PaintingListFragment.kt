package com.example.wikiart.ui.paintings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import com.example.wikiart.R
import com.example.wikiart.databinding.FragmentPaintingListBinding
import com.example.wikiart.model.PaintingCategory

class PaintingListFragment : Fragment() {

    private var _binding: FragmentPaintingListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaintingListViewModel by viewModels()
    private lateinit var adapter: PaintingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaintingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PaintingAdapter(viewModel.layout)
        binding.paintingRecyclerView.adapter = adapter
        binding.paintingRecyclerView.layoutManager = layoutManagerFor(viewModel.layout)

        binding.categoryButton.setOnClickListener { openOptions() }

        viewModel.paintings.observe(viewLifecycleOwner) { adapter.submitList(it) }
        viewModel.loading.observe(viewLifecycleOwner) { binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE }
        viewModel.error.observe(viewLifecycleOwner) { err ->
            err?.let { Toast.makeText(requireContext(), it.localizedMessage ?: "Load failed", Toast.LENGTH_SHORT).show() }
        }

        viewModel.loadNext()

        binding.paintingRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        OptionsBottomSheetFragment.newInstance(viewModel.category, viewModel.layout).apply {
            setListener(object : OptionsBottomSheetFragment.Listener {
                override fun onOptionsSelected(category: PaintingCategory, layout: PaintingAdapter.Layout) {
                    if (viewModel.category != category) {
                        viewModel.setCategory(category)
                    }
                    if (viewModel.layout != layout) {
                        viewModel.setLayout(layout)
                        adapter.setLayout(layout)
                        binding.paintingRecyclerView.layoutManager = layoutManagerFor(layout)
                        binding.paintingRecyclerView.scrollToPosition(0)
                    }
                }
            })
        }.show(parentFragmentManager, "options")
    }

    private fun layoutManagerFor(layout: PaintingAdapter.Layout): RecyclerView.LayoutManager = when(layout) {
        PaintingAdapter.Layout.LIST -> LinearLayoutManager(requireContext())
        PaintingAdapter.Layout.GRID -> GridLayoutManager(requireContext(), 2)
        PaintingAdapter.Layout.SHEET -> GridLayoutManager(requireContext(), 2)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
