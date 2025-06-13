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
import com.wikiart.model.LayoutType

class PaintingAdapter(
    var layoutType: LayoutType = LayoutType.LIST,
    private val onItemClick: (Painting, ImageView) -> Unit = { _, _ -> }
) : PagingDataAdapter<Painting, PaintingAdapter.PaintingViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int = layoutType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaintingViewHolder {
        val layout = when (layoutType) {
            LayoutType.SHEET -> R.layout.item_painting_sheet
            LayoutType.COLUMN -> R.layout.item_painting_grid
            else -> R.layout.list_item_painting
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return PaintingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaintingViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class PaintingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val artistText: TextView? = itemView.findViewById(R.id.artistText)
        private val yearText: TextView? = itemView.findViewById(R.id.yearText)
        private val paintingImage: ImageView = itemView.findViewById(R.id.paintingImage)

        fun bind(painting: Painting) {
            titleText.text = painting.title
            artistText?.text = painting.artistName
            yearText?.text = painting.year

            if (layoutType == LayoutType.COLUMN) {
                val dm = itemView.resources.displayMetrics
                val itemWidth = dm.widthPixels / 2
                val padding = itemView.resources.getDimensionPixelSize(
                    R.dimen.painting_grid_image_padding
                ) * 2
                val width = itemWidth - padding
                val ratio = painting.height.toFloat() / painting.width
                paintingImage.layoutParams = paintingImage.layoutParams.apply {
                    height = (width * ratio).toInt()
                }
            }

            paintingImage.load(painting.thumbUrl)

            itemView.setOnClickListener { onItemClick(painting, paintingImage) }
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
