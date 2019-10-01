package com.antarikshc.theguardiannews.datasource

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.antarikshc.theguardiannews.model.NewsData
import com.antarikshc.theguardiannews.util.ConnectAPI

class NewsLoader(context : Context, val url : String) : AsyncTaskLoader<ArrayList<NewsData>>(context) {

    override fun onStartLoading() {
        forceLoad()
    }

    override fun loadInBackground(): ArrayList<NewsData>? {
        return url.let {
            ConnectAPI.fetchNewsData(it)
        }
    }
}