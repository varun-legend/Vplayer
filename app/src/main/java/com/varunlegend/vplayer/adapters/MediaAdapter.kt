package com.varunlegend.vplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.varunlegend.vplayer.MediaItemModel
import com.varunlegend.vplayer.databinding.ItemMediaBinding

/** Binds MediaItemModel into RecyclerView items */
class MediaAdapter(
    private val items: List<MediaItemModel>,
    private val onClick: (MediaItemModel) -> Unit
) : RecyclerView.Adapter<MediaAdapter.VH>() {

    inner class VH(val b: ItemMediaBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: MediaItemModel) {
            b.tvTitle.text = item.title
            b.tvDuration.text = item.duration
            b.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size
}
