package com.antarikshc.theguardiannews.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import com.antarikshc.theguardiannews.R
import com.antarikshc.theguardiannews.datasource.NewsLoader
import com.antarikshc.theguardiannews.model.NewsData
import com.antarikshc.theguardiannews.util.Master
import kotlinx.android.synthetic.main.world_fragment.*

class WorldFragment : Fragment(), LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    // Global Params
    private var scrollState = 0
    private lateinit var worldNewsAdapter: CustomAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //inflate the fragment with world_fragment layout
        return inflater.inflate(R.layout.world_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNewsList()

        // URL to fetch data for World news
        buildUriParams()

        // loaderManager = activity?.supportLoaderManager

        //this is to prevent loader from re-fetching the data
        if (worldNewsAdapter.count == 0 && Master.checkNet(context)) {
            executeLoader()
        } else {
            load_spin.visibility = View.GONE
            empty_view.setText(R.string.no_network)
        }

        world_news_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val currentData = worldNewsAdapter.getItem(position)

            if (currentData != null) {
                try {
                    //Explicit Intent
                    val webActivityIntent = Intent(context, WebviewActivity::class.java)
                    webActivityIntent.putExtra("url", currentData.webUrl)
                    startActivity(webActivityIntent)
                } catch (e: Exception) {
                    //Implicit Intent
                    val webUri = Uri.parse(currentData.webUrl)
                    val webBrowserIntent = Intent(Intent.ACTION_VIEW, webUri)
                    activity?.run {
                        if (webBrowserIntent.resolveActivity(packageManager) != null) {
                            startActivity(webBrowserIntent)
                        }
                    }
                }
            }
        }

        world_refresh.setOnRefreshListener {
            empty_view.text = ""

            WORLD_NEWS_LOADER += 1
            world_refresh.isRefreshing = true

            if (Master.checkNet(context)) {
                executeLoader()
            } else {
                load_spin.visibility = View.GONE
                empty_view.setText(R.string.no_network)
            }

            destroyLoader(WORLD_NEWS_LOADER - 1)

            world_refresh.isRefreshing = false
        }

    }

    /**
     * Add URL params to call API
     */
    private fun buildUriParams() {
        worldUri.appendQueryParameter("show-editors-picks", "true")
        worldUri.appendQueryParameter("format", "json")
        worldUri.appendQueryParameter("from-date", "2017-03-01")
        worldUri.appendQueryParameter("show-fields", "thumbnail,headline,byline")
        worldUri.appendQueryParameter("show-tags", "contributor")
        worldUri.appendQueryParameter("api-key", Master.apiKey)
    }

    /**
     * Initiate News List View, Adapter and add animation
     * Set Empty View
     */
    private fun setupNewsList() {
        world_news_list.animate().alpha(0.1f).duration = 400

        // Initiate CustomAdapter and set it for worldNewsList
        activity?.run {
            worldNewsAdapter = CustomAdapter(applicationContext, ArrayList())
        }
        world_news_list.adapter = worldNewsAdapter

        // Set empty text view for a proper msg to user
        world_news_list.emptyView = empty_view
    }

    /**
     * This is to prevent ListView losing the scroll position
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        try {
            outState.putInt("CURRENT_SCROLL", world_news_list.firstVisiblePosition)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getInt("CURRENT_SCROLL")
            world_news_list.setSelection(scrollState)
        }
    }

    //Initiate and destroy loader methods to be called after search is submitted
    private fun executeLoader() {
        loaderManager.initLoader(WORLD_NEWS_LOADER, null, this)
    }

    private fun destroyLoader(id: Int) {
        loaderManager.destroyLoader(id)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): androidx.loader.content.Loader<ArrayList<NewsData>> {
        return NewsLoader(activity!!, worldUri.toString())
    }

    override fun onLoadFinished(loader: androidx.loader.content.Loader<ArrayList<NewsData>>, news: ArrayList<NewsData>?) {
        empty_view.setText(R.string.no_news)

        // Clear the adapter of previous books data
        worldNewsAdapter.clear()

        load_spin.visibility = View.GONE

        if (news != null && news.isNotEmpty()) {
            worldNewsAdapter.addAll(news)
            world_news_list.animate().alpha(1.0f).duration = 400
            world_news_list.setSelection(scrollState)
        }
    }

    override fun onLoaderReset(loader: androidx.loader.content.Loader<ArrayList<NewsData>>) {
        worldNewsAdapter.clear()
    }

    companion object {
        // we are using different loaders for each tab
        private var WORLD_NEWS_LOADER = 1
        private val worldUri = Master.worldUri
    }
}