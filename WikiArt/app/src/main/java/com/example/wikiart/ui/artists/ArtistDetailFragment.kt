package com.example.wikiart.ui.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wikiart.R
import com.example.wikiart.databinding.FragmentArtistDetailBinding
import com.example.wikiart.ui.paintings.PaintingAdapter
import com.example.wikiart.ui.paintings.PaintingDetailFragment

class ArtistDetailFragment : Fragment() {

    private var _binding: FragmentArtistDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArtistDetailViewModel by viewModels {
        ArtistDetailViewModel.Factory(requireArguments().getString(ARG_ARTIST_ID)!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PaintingAdapter(PaintingAdapter.Layout.GRID) { painting ->
            val bundle = Bundle().apply { putString(PaintingDetailFragment.ARG_PAINTING_ID, painting.id) }
            findNavController().navigate(R.id.action_artistDetailFragment_to_paintingDetailFragment, bundle)
        }
        binding.paintingsRecyclerView.adapter = adapter
        binding.paintingsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.biography.observe(viewLifecycleOwner) { binding.biographyText.text = it }
        viewModel.paintings.observe(viewLifecycleOwner) { adapter.submitList(it) }
        viewModel.loading.observe(viewLifecycleOwner) { binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_ARTIST_ID = "artist_id"
    }
}
