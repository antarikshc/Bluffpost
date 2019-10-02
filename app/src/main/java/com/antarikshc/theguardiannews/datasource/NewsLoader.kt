package com.antarikshc.theguardiannews.datasource

import android.content.Context
import com.antarikshc.theguardiannews.model.NewsData
import com.antarikshc.theguardiannews.util.ConnectAPI
import java.util.*

class NewsLoader(context: Context, private val mUrl: String?) :
        androidx.loader.content.AsyncTaskLoader<ArrayList<NewsData>>(context) {

    override fun onStartLoading() {
        forceLoad()
    }

    override fun loadInBackground(): ArrayList<NewsData>? {
        return mUrl?.let {
            ConnectAPI.fetchNewsData(it)
        }
    }
}
