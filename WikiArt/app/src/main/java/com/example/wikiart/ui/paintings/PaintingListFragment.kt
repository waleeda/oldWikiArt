package com.example.wikiart.ui.paintings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wikiart.R
import com.example.wikiart.databinding.FragmentPaintingListBinding
import com.example.wikiart.model.PaintingCategory

class PaintingListFragment : Fragment() {

    private var _binding: FragmentPaintingListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaintingListViewModel by viewModels()
    private lateinit var adapter: PaintingAdapter

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

        adapter = PaintingAdapter(PaintingAdapter.Layout.LIST)
        binding.paintingRecyclerView.adapter = adapter
        binding.paintingRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        setupCategorySpinner(binding.categorySpinner)
        binding.layoutButton.setOnClickListener { toggleLayout() }

        viewModel.paintings.observe(viewLifecycleOwner) { adapter.submitList(it) }

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

    private fun setupCategorySpinner(spinner: Spinner) {
        val items = PaintingCategory.values().map { getString(it.titleRes) }
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        spinner.setSelection(PaintingCategory.values().indexOf(viewModel.category))
        spinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                val cat = PaintingCategory.values()[position]
                viewModel.setCategory(cat)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        })
    }

    private fun toggleLayout() {
        val next = when(adapter.layout) {
            PaintingAdapter.Layout.LIST -> PaintingAdapter.Layout.GRID
            PaintingAdapter.Layout.GRID -> PaintingAdapter.Layout.SHEET
            PaintingAdapter.Layout.SHEET -> PaintingAdapter.Layout.LIST
        }
        adapter.setLayout(next)
        val manager = when(next) {
            PaintingAdapter.Layout.LIST -> LinearLayoutManager(requireContext())
            PaintingAdapter.Layout.GRID -> GridLayoutManager(requireContext(), 2)
            PaintingAdapter.Layout.SHEET -> GridLayoutManager(requireContext(), 2)
        }
        binding.paintingRecyclerView.layoutManager = manager
        binding.paintingRecyclerView.scrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
