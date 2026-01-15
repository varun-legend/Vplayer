package com.varunlegend.vplayer.adapters

import android.content.ContentUris
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.varunlegend.vplayer.MediaItemModel
import com.varunlegend.vplayer.R
import com.varunlegend.vplayer.databinding.ItemMediaBinding

class MediaAdapter(
    private val items: List<MediaItemModel>,
    private val onClick: (MediaItemModel) -> Unit
) : RecyclerView.Adapter<MediaAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMediaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class VH(private val b: ItemMediaBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: MediaItemModel) {
            // 1) Set the name and duration
            b.tvName.text = item.name
            b.tvDuration.text = item.duration

            // 2) Load a thumbnail from MediaStore
            val uri = item.uri
            val id  = ContentUris.parseId(uri)
            val thumbUri = MediaStore.Video.Thumbnails.getContentUri(
                MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI.toString()
            )
            Glide.with(b.ivThumb)
                .load(MediaStore.Video.Thumbnails.getThumbnail(
                    b.ivThumb.context.contentResolver,
                    id,
                    MediaStore.Video.Thumbnails.MINI_KIND,
                    null
                ))
                .placeholder(R.drawable.ic_video_placeholder)
                .centerCrop()
                .into(b.ivThumb)

            // 3) Click callback
            b.root.setOnClickListener { onClick(item) }
        }
    }
}
