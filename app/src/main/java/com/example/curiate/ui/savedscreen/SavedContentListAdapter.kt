package com.example.curiate.ui.savedscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.curiate.R
import com.example.curiate.domain.models.SavedContentData

class SavedContentListAdapter(private val onPostClick: (url: String) -> Unit): ListAdapter<SavedContentData, SavedContentListAdapter.SavedContentViewHolder>(SavedContentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedContentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.saved_content_card, parent, false)
        return SavedContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedContentViewHolder, position: Int) {
        val item = getItem(position)
        holder.contentTitleTextView.text = item.title
        holder.contentUrlTextView.text = item.contentUrl
        Glide.with(holder.contentImageView.context)
            .load(item.imageUrl)
            .into(holder.contentImageView)

        holder.itemView.setOnClickListener {
            onPostClick(item.contentUrl)
        }
    }

    class SavedContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentImageView: ImageView = itemView.findViewById(R.id.content_image)
        val contentTitleTextView: TextView = itemView.findViewById(R.id.content_title)
        val contentUrlTextView: TextView = itemView.findViewById(R.id.content_url)
    }

    class SavedContentDiffCallback: DiffUtil.ItemCallback<SavedContentData>() {
        override fun areItemsTheSame(oldItem: SavedContentData, newItem: SavedContentData): Boolean {
            return oldItem.contentUrl == newItem.contentUrl
        }

        override fun areContentsTheSame(oldItem: SavedContentData, newItem: SavedContentData): Boolean {
            return oldItem == newItem
        }

    }
}