package com.example.wikiart.ui.paintings

import android.content.Intent
import android.net.Uri
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
import com.example.wikiart.api.FavoritesRepository
import com.example.wikiart.api.getLanguage
import com.example.wikiart.databinding.FragmentPaintingDetailBinding
import androidx.lifecycle.lifecycleScope
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch

class PaintingDetailFragment : Fragment() {

    private var _binding: FragmentPaintingDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaintingDetailViewModel by viewModels {
        PaintingDetailViewModel.Factory(
            requireArguments().getString(ARG_PAINTING_ID)!!,
            getLanguage(),
        )
    }

    private val favoritesRepository by lazy { FavoritesRepository(requireContext()) }

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
                binding.paintingImage.contentDescription = getString(R.string.content_description_painting, it.title, it.artistName)
                binding.paintingTitle.text = it.title
                binding.paintingArtist.text = it.artistName
                binding.paintingYear.text = it.year
                binding.paintingSize.text = formatDimensions(it.width, it.height)
                viewLifecycleOwner.lifecycleScope.launch {
                    updateFavoriteButton(favoritesRepository.isFavorite(it.id))
                }
            }
        }

        viewModel.related.observe(viewLifecycleOwner) {
            relatedAdapter.submitList(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        binding.favoriteButton.setOnClickListener {
            val painting = viewModel.painting.value ?: return@setOnClickListener
            viewLifecycleOwner.lifecycleScope.launch {
                val isFav = favoritesRepository.toggleFavorite(painting.id)
                updateFavoriteButton(isFav)
            }
        }

        binding.shareButton.setOnClickListener {
            val painting = viewModel.painting.value ?: return@setOnClickListener
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "${painting.title} - ${painting.paintingUrl}")
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_painting)))
        }

        binding.buyButton.setOnClickListener {
            val painting = viewModel.painting.value ?: return@setOnClickListener
            val uri = Uri.parse(painting.paintingUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_PAINTING_ID = "painting_id"
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        binding.favoriteButton.apply {
            text = if (isFavorite) getString(R.string.unfavorite) else getString(R.string.favorite)
            icon = ContextCompat.getDrawable(
                requireContext(),
                if (isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
            )
        }
    }

    private fun formatDimensions(width: Int, height: Int): String {
        return if (width > 0 && height > 0) "${width}×${height} cm" else ""
    }
}
