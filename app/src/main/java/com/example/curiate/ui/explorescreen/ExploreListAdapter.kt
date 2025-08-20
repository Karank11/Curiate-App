package com.example.curiate.ui.explorescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.curiate.R
import com.example.curiate.domain.models.NewsData

class ExploreListAdapter: ListAdapter<NewsData, ExploreListAdapter.ExploreItemViewHolder>(ExploreItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.explore_article_card, parent, false)
        return ExploreItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExploreItemViewHolder, position: Int) {
        val articleItem = getItem(position)
        holder.sourceNameTextView.text = articleItem.sourceName
        holder.timeTextView.text = articleItem.publishedAt
        holder.titleTextView.text = articleItem.title
        holder.contentTextView.text = articleItem.content
        holder.authorTextView.text = articleItem.author
    }

    class ExploreItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.article_image)
        val sourceNameTextView = itemView.findViewById<TextView>(R.id.source_name_text)
        val timeTextView = itemView.findViewById<TextView>(R.id.time_ago_text)
        val titleTextView = itemView.findViewById<TextView>(R.id.article_title_text)
        val contentTextView = itemView.findViewById<TextView>(R.id.article_summary_text)
        val authorTextView = itemView.findViewById<TextView>(R.id.author_text)
    }

    class ExploreItemDiffCallback: DiffUtil.ItemCallback<NewsData>() {
        override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
            return oldItem == newItem
        }
    }
}