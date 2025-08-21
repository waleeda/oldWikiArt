package com.example.wikiart.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.wikiart.R
import com.example.wikiart.model.Artist
import com.example.wikiart.model.Painting

sealed class SearchItem {
    data class Header(val title: String) : SearchItem()
    data class PaintingItem(val painting: Painting) : SearchItem()
    data class ArtistItem(val artist: Artist) : SearchItem()
    data class SuggestionItem(val term: String) : SearchItem()
}

class SearchAdapter(
    private val onPainting: (Painting) -> Unit,
    private val onArtist: (Artist) -> Unit,
    private val onSuggestion: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<SearchItem>()

    fun submitList(data: List<SearchItem>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = when(items[position]) {
        is SearchItem.Header -> 0
        is SearchItem.PaintingItem -> 1
        is SearchItem.ArtistItem -> 2
        is SearchItem.SuggestionItem -> 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when(viewType) {
        0 -> HeaderVH(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))
        1 -> PaintingVH(LayoutInflater.from(parent.context).inflate(R.layout.item_painting_list, parent, false))
        2 -> ArtistVH(LayoutInflater.from(parent.context).inflate(R.layout.item_artist_list, parent, false))
        else -> SuggestionVH(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = items[position]) {
            is SearchItem.Header -> (holder as HeaderVH).bind(item.title)
            is SearchItem.PaintingItem -> (holder as PaintingVH).bind(item.painting, onPainting)
            is SearchItem.ArtistItem -> (holder as ArtistVH).bind(item.artist, onArtist)
            is SearchItem.SuggestionItem -> (holder as SuggestionVH).bind(item.term, onSuggestion)
        }
    }

    override fun getItemCount(): Int = items.size

    class HeaderVH(view: View) : RecyclerView.ViewHolder(view) {
        private val text: TextView = view.findViewById(android.R.id.text1)
        fun bind(title: String) { text.text = title }
    }

    class SuggestionVH(view: View) : RecyclerView.ViewHolder(view) {
        private val text: TextView = view.findViewById(android.R.id.text1)
        fun bind(term: String, listener: (String) -> Unit) {
            text.text = term
            itemView.setOnClickListener { listener(term) }
        }
    }

    class PaintingVH(view: View) : RecyclerView.ViewHolder(view) {
        private val image: android.widget.ImageView = view.findViewById(R.id.paintingImage)
        private val title: TextView? = view.findViewById(R.id.paintingTitle)
        private val artist: TextView? = view.findViewById(R.id.paintingArtist)
        fun bind(p: Painting, listener: (Painting) -> Unit) {
            image.load(p.imageUrl())
            title?.text = p.title
            artist?.text = p.artistName
            itemView.setOnClickListener { listener(p) }
        }
    }

    class ArtistVH(view: View) : RecyclerView.ViewHolder(view) {
        private val image: android.widget.ImageView = view.findViewById(R.id.artistImage)
        private val name: TextView = view.findViewById(R.id.artistName)
        fun bind(a: Artist, listener: (Artist) -> Unit) {
            image.load(a.image)
            name.text = a.title
            itemView.setOnClickListener { listener(a) }
        }
    }
}
