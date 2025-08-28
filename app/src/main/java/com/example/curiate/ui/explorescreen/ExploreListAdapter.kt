package com.example.curiate.ui.explorescreen

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
import com.example.curiate.domain.models.NewsArticle

class ExploreListAdapter(private val onPostClick: (url: String) -> Unit): ListAdapter<NewsArticle, ExploreListAdapter.ExploreItemViewHolder>(ExploreItemDiffCallback()) {

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
        Glide.with(holder.imageView.context)
            .load(articleItem.imageUrl)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            onPostClick(articleItem.url ?: "")
        }
    }

    class ExploreItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val sourceNameTextView: TextView = itemView.findViewById(R.id.source_name_text)
        val timeTextView: TextView = itemView.findViewById(R.id.time_ago_text)
        val titleTextView: TextView = itemView.findViewById(R.id.article_title_text)
        val contentTextView: TextView = itemView.findViewById(R.id.article_summary_text)
        val authorTextView: TextView = itemView.findViewById(R.id.author_text)
        val imageView: ImageView = itemView.findViewById(R.id.article_image)
    }

    class ExploreItemDiffCallback: DiffUtil.ItemCallback<NewsArticle>() {
        override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
            return oldItem == newItem
        }
    }
}