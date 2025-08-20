package com.example.wikiart.ui.paintings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.example.wikiart.R
import com.example.wikiart.databinding.FragmentPaintingDetailBinding

class PaintingDetailFragment : Fragment() {

    private var _binding: FragmentPaintingDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaintingDetailViewModel by viewModels {
        PaintingDetailViewModel.Factory(requireArguments().getString(ARG_PAINTING_ID)!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaintingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val relatedAdapter = PaintingAdapter(PaintingAdapter.Layout.GRID) { painting ->
            val bundle = Bundle().apply { putString(ARG_PAINTING_ID, painting.id) }
            findNavController().navigate(R.id.action_paintingDetailFragment_self, bundle)
        }
        binding.relatedRecyclerView.adapter = relatedAdapter
        binding.relatedRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.painting.observe(viewLifecycleOwner) { painting ->
            painting?.let {
                binding.paintingImage.load(it.imageUrl())
                binding.paintingTitle.text = it.title
                binding.paintingArtist.text = it.artistName
            }
        }

        viewModel.related.observe(viewLifecycleOwner) {
            relatedAdapter.submitList(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        // Buttons currently have no functionality
        binding.favoriteButton.setOnClickListener { }
        binding.shareButton.setOnClickListener { }
        binding.buyButton.setOnClickListener { }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_PAINTING_ID = "painting_id"
    }
}
