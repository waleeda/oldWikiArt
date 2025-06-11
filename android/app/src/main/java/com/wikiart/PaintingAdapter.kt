package com.wikiart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.paging.PagingDataAdapter

class PaintingAdapter : PagingDataAdapter<Painting, PaintingAdapter.PaintingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaintingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_painting, parent, false)
        return PaintingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaintingViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class PaintingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.titleText)

        fun bind(painting: Painting) {
            titleText.text = painting.title
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
