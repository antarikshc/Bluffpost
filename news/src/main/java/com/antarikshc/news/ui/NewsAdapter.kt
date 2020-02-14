package com.antarikshc.news.ui

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.antarikshc.bluffpost.utils.getRelativeTime
import com.antarikshc.news.R
import com.antarikshc.news.models.News
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import kotlinx.android.synthetic.main.layout_news_item.view.*

class NewsAdapter(private val glide: RequestManager) :
    PagedListAdapter<News, NewsAdapter.ViewHolder>(
        NewsDC()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_news_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun swapData(data: PagedList<News>) = submitList(data)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION) return
        }

        fun bind(item: News?) = with(itemView) {

            if (item?.content != null) {
                glide.load(item.content.thumbnailUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.rounded_placeholder)
                    .transform(RoundedCorners(18))
                    .transition(withCrossFade())
                    .into(img_news_thumbnail)

                text_news_title.text = item.content.headline

                if (item.getAuthor() != null) {
                    val byline = "${item.getAuthor()} • ${item.publicationDate?.getRelativeTime()}"
                    text_news_byline.text = byline
                } else {
                    text_news_byline.text = item.publicationDate?.getRelativeTime()
                }
            }

        }
    }

    private class NewsDC : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean =
            oldItem == newItem
    }
}