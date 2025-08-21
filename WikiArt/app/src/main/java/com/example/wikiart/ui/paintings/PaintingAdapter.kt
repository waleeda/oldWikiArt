package com.example.wikiart.ui.paintings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.wikiart.R
import com.example.wikiart.model.Painting

class PaintingAdapter(layout: Layout, private val listener: ((Painting) -> Unit)? = null) : RecyclerView.Adapter<PaintingAdapter.VH>() {

    var layout: Layout = layout
        private set

    enum class Layout { LIST, GRID, SHEET }

    private val items = mutableListOf<Painting>()

    fun submitList(data: List<Painting>) {
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
            Layout.LIST -> R.layout.item_painting_list
            Layout.GRID -> R.layout.item_painting_grid
            Layout.SHEET -> R.layout.item_painting_sheet
        }
        val view = LayoutInflater.from(parent.context).inflate(res, parent, false)
        return VH(view, layout)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    class VH(view: View, private val layout: Layout) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.paintingImage)
        private val title: TextView? = view.findViewById(R.id.paintingTitle)
        private val artist: TextView? = view.findViewById(R.id.paintingArtist)

        fun bind(p: Painting, listener: ((Painting) -> Unit)?) {
            if (layout == Layout.GRID) {
                val lp = image.layoutParams
                lp.width = p.width
                lp.height = p.height
                image.layoutParams = lp
            }
            image.load(p.imageUrl())
            title?.text = p.title
            artist?.text = p.artistName
            itemView.setOnClickListener { listener?.invoke(p) }
        }
    }
}
