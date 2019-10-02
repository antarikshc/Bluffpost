package com.antarikshc.theguardiannews.datasource

import android.content.Context
import android.support.v4.content.AsyncTaskLoader

import com.antarikshc.theguardiannews.model.NewsData
import com.antarikshc.theguardiannews.util.ConnectAPI

import java.util.ArrayList

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
