package com.wikiart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class RelatedPaintingAdapter(
    private val onItemClick: (Painting) -> Unit = {}
) : androidx.recyclerview.widget.ListAdapter<Painting, RelatedPaintingAdapter.ViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_related_painting, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.paintingImage)
        private val title: TextView = itemView.findViewById(R.id.titleText)

        fun bind(painting: Painting) {
            title.text = painting.title
            image.load(painting.image)
            itemView.setOnClickListener { onItemClick(painting) }
        }
    }

    private class Diff : androidx.recyclerview.widget.DiffUtil.ItemCallback<Painting>() {
        override fun areItemsTheSame(oldItem: Painting, newItem: Painting): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Painting, newItem: Painting): Boolean = oldItem == newItem
    }
}
