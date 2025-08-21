package com.example.wikiart.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wikiart.R
import com.example.wikiart.databinding.FragmentSearchBinding
import com.example.wikiart.ui.artists.ArtistDetailFragment
import com.example.wikiart.ui.paintings.PaintingDetailFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchAdapter
    private var autocompleteJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SearchAdapter(
            onPainting = { painting ->
                val bundle = Bundle().apply { putString(PaintingDetailFragment.ARG_PAINTING_ID, painting.id) }
                findNavController().navigate(R.id.action_search_to_paintingDetailFragment, bundle)
            },
            onArtist = { artist ->
                val bundle = Bundle().apply { putString(ArtistDetailFragment.ARG_ARTIST_PATH, artist.artistUrl ?: "") }
                findNavController().navigate(R.id.action_search_to_artistDetailFragment, bundle)
            },
            onSuggestion = { term ->
                binding.searchBar.setQuery(term, false)
                viewModel.search(term)
            }
        )

        binding.resultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.resultsRecyclerView.adapter = adapter
        binding.resultsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lm = recyclerView.layoutManager as LinearLayoutManager
                val last = lm.findLastVisibleItemPosition()
                if (last >= adapter.itemCount - 5) {
                    viewModel.loadMorePaintings()
                    viewModel.loadMoreArtists()
                }
            }
        })

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search(query)
                binding.searchBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                autocompleteJob?.cancel()
                autocompleteJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    viewModel.autocomplete(newText)
                }
                return true
            }
        })

        viewModel.items.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
