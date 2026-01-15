/*
 MIT License
 
 Copyright (c) 2026 Varun Prasath
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
*/

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
            
            b.tvName.text = item.name
            b.tvDuration.text = item.duration

            
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

            
            b.root.setOnClickListener { onClick(item) }
        }
    }
}
