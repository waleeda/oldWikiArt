package com.wikiart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class RelatedPaintingAdapter(
    private val onItemClick: (Painting, ImageView) -> Unit = { _, _ -> }
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
        private val artist: TextView? = itemView.findViewById(R.id.artistText)
        private val year: TextView? = itemView.findViewById(R.id.yearText)

        fun bind(painting: Painting) {
            title.text = painting.title
            artist?.text = painting.artistName
            year?.text = painting.year

            val height = itemView.resources.getDimensionPixelSize(R.dimen.related_image_height)
            val width = if (painting.height > 0) {
                height * painting.width / painting.height
            } else {
                height
            }
            image.layoutParams = image.layoutParams.apply {
                this.height = height
                this.width = width
            }

            image.load(painting.thumbUrl)
            itemView.setOnClickListener { onItemClick(painting, image) }
        }
    }

    private class Diff : androidx.recyclerview.widget.DiffUtil.ItemCallback<Painting>() {
        override fun areItemsTheSame(oldItem: Painting, newItem: Painting): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Painting, newItem: Painting): Boolean = oldItem == newItem
    }
}
