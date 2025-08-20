package com.example.wikiart.ui.artists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.wikiart.R
import com.example.wikiart.model.Artist

class ArtistAdapter(private var layout: Layout, private val listener: ((Artist) -> Unit)? = null) : RecyclerView.Adapter<ArtistAdapter.VH>() {

    enum class Layout { LIST, GRID }

    private val items = mutableListOf<Artist>()

    fun submitList(data: List<Artist>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    fun setLayout(l: Layout) {
        layout = l
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = layout.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val res = when(layout) {
            Layout.LIST -> R.layout.item_artist_list
            Layout.GRID -> R.layout.item_artist_grid
        }
        val view = LayoutInflater.from(parent.context).inflate(res, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.artistImage)
        private val name: TextView = view.findViewById(R.id.artistName)

        fun bind(artist: Artist, listener: ((Artist) -> Unit)?) {
            image.load(artist.image)
            name.text = artist.title
            itemView.setOnClickListener { listener?.invoke(artist) }
        }
    }
}
