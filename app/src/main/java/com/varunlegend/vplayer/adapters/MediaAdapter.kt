package com.varunlegend.vplayer.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.varunlegend.vplayer.MediaItemModel
import com.varunlegend.vplayer.databinding.ItemMediaBinding

class MediaAdapter(private val items: List<MediaItemModel>, private val onClick: (MediaItemModel) -> Unit)
    : RecyclerView.Adapter<MediaAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MediaItemModel) {
            binding.tvTitle.text = item.title
            binding.tvDuration.text = item.duration
            binding.root.setOnClickListener { onClick(item) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size
    }
