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
) : RecyclerView.Adapter<RelatedPaintingAdapter.ViewHolder>() {

    private val items = mutableListOf<Painting>()

    fun submitList(list: List<Painting>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_related_painting, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.paintingImage)
        private val title: TextView = itemView.findViewById(R.id.titleText)

        fun bind(painting: Painting) {
            title.text = painting.title
            image.load(painting.image)
            itemView.setOnClickListener { onItemClick(painting) }
        }
    }
}
