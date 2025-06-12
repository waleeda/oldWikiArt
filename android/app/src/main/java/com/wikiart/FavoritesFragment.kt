package com.wikiart

import android.content.Intent
import android.app.ActivityOptions
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wikiart.data.FavoritesRepository
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {
    private val adapter = PaintingAdapter { painting ->
        val intent = Intent(requireContext(), PaintingDetailActivity::class.java)
        intent.putExtra(PaintingDetailActivity.EXTRA_PAINTING, painting)
        val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity())
        startActivity(intent, options.toBundle())
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val repository = FavoritesRepository(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            repository.favoritesFlow().collect { list ->
                adapter.submitData(lifecycle, PagingData.from(list))
            }
        }
    }
}
