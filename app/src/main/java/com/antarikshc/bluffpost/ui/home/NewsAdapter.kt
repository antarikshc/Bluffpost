package com.antarikshc.bluffpost.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.CachePolicy
import coil.transform.RoundedCornersTransformation
import com.antarikshc.bluffpost.R
import com.antarikshc.bluffpost.models.News
import kotlinx.android.synthetic.main.layout_news_item.view.*

class NewsAdapter : ListAdapter<News, NewsAdapter.ViewHolder>(NewsDC()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_news_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun swapData(data: List<News>) = submitList(data.toMutableList())

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION) return
        }

        fun bind(item: News) = with(itemView) {

            img_news_thumbnail.load(item.content.thumbnailUrl) {
                diskCachePolicy(CachePolicy.ENABLED)
                crossfade(true)
                placeholder(R.mipmap.media_place_holder)
                transformations(RoundedCornersTransformation(16F))
            }

            text_news_title.text = item.content.headline
        }
    }

    private class NewsDC : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean =
            oldItem == newItem
    }
}