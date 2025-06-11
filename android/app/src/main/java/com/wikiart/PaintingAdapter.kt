package com.wikiart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import coil.load
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.paging.PagingDataAdapter

class PaintingAdapter(
    private val onItemClick: (Painting) -> Unit = {}
) : PagingDataAdapter<Painting, PaintingAdapter.PaintingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaintingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_painting, parent, false)
        return PaintingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaintingViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class PaintingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val paintingImage: ImageView = itemView.findViewById(R.id.paintingImage)

        fun bind(painting: Painting) {
            titleText.text = painting.title
            paintingImage.load(painting.image)

            itemView.setOnClickListener { onItemClick(painting) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Painting>() {
            override fun areItemsTheSame(oldItem: Painting, newItem: Painting): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Painting, newItem: Painting): Boolean =
                oldItem == newItem
        }
    }
}
