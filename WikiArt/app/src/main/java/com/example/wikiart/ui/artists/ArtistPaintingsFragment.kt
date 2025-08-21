package com.example.wikiart.ui.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wikiart.R
import com.example.wikiart.databinding.FragmentArtistPaintingsBinding
import com.example.wikiart.ui.paintings.PaintingAdapter
import com.example.wikiart.ui.paintings.PaintingDetailFragment

class ArtistPaintingsFragment : Fragment() {

    private var _binding: FragmentArtistPaintingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArtistPaintingsViewModel by viewModels {
        ArtistPaintingsViewModel.Factory(requireArguments().getString(ARG_ARTIST_PATH)!!)
    }

    private lateinit var adapter: PaintingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArtistPaintingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PaintingAdapter(PaintingAdapter.Layout.GRID) { painting ->
            val bundle = Bundle().apply { putString(PaintingDetailFragment.ARG_PAINTING_ID, painting.id) }
            findNavController().navigate(R.id.action_artistPaintingsFragment_to_paintingDetailFragment, bundle)
        }
        binding.paintingsRecyclerView.adapter = adapter
        binding.paintingsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.paintings.observe(viewLifecycleOwner) { adapter.submitList(it) }
        viewModel.loading.observe(viewLifecycleOwner) { binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE }
        viewModel.error.observe(viewLifecycleOwner) { err ->
            err?.let {
                Toast.makeText(
                    requireContext(),
                    it.localizedMessage ?: getString(R.string.error_generic),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.loadNext()

        binding.paintingsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val manager = recyclerView.layoutManager as GridLayoutManager
                val lastVisible = manager.findLastVisibleItemPosition()
                if (lastVisible >= adapter.itemCount - 5) {
                    viewModel.loadNext()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_ARTIST_PATH = "artist_path"
    }
}
