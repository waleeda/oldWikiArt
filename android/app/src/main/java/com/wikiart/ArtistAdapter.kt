package com.wikiart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import android.os.Build
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderEffectBlur
import com.wikiart.model.Artist

class ArtistAdapter(
    private val onItemClick: (Artist) -> Unit = {}
) : PagingDataAdapter<Artist, ArtistAdapter.ArtistViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_artist, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.artistName)
        private val worksText: TextView = itemView.findViewById(R.id.artistWorks)
        private val nationText: TextView = itemView.findViewById(R.id.artistNation)
        private val artistImage: ImageView = itemView.findViewById(R.id.artistImage)
        private val blurView: BlurView = itemView.findViewById(R.id.infoBlur)

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val root = itemView.rootView.findViewById<ViewGroup>(android.R.id.content)
                val attrs = itemView.context.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
                val windowBackground = attrs.getDrawable(0)
                attrs.recycle()
                blurView.setupWith(root)
                    .setFrameClearDrawable(windowBackground)
                    .setBlurAlgorithm(RenderEffectBlur())
                    .setBlurRadius(itemView.resources.getDimension(R.dimen.detail_blur_radius))
                    .setHasFixedTransformationMatrix(true)
            }
        }

        fun bind(artist: Artist) {
            nameText.text = artist.title
            worksText.text = artist.totalWorksTitle?.capitalize()
            nationText.text = listOfNotNull(artist.nation, artist.year).joinToString(", ")
            artistImage.load(artist.image)
            itemView.setOnClickListener { onItemClick(artist) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Artist>() {
            override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean =
                oldItem == newItem
        }
    }
}
