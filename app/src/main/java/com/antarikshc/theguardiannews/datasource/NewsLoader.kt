package com.antarikshc.theguardiannews.datasource

import android.content.Context
import androidx.loader.content.AsyncTaskLoader
import com.antarikshc.theguardiannews.model.NewsData
import com.antarikshc.theguardiannews.util.ConnectAPI

class NewsLoader(context: Context, private val mUrl: String?) :
        AsyncTaskLoader<ArrayList<NewsData>>(context) {

    override fun onStartLoading() {
        forceLoad()
    }

    override fun loadInBackground(): ArrayList<NewsData>? {
        return mUrl?.let {
            ConnectAPI.fetchNewsData(it)
        }
    }
}
