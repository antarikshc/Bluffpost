package com.antarikshc.theguardiannews.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import com.antarikshc.theguardiannews.R
import com.antarikshc.theguardiannews.datasource.NewsLoader
import com.antarikshc.theguardiannews.model.NewsData
import com.antarikshc.theguardiannews.util.Master

class WorldFragment : Fragment(), LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    // we are using different loaders for each tab
    private var WORLD_NEWS_LOADER = 1

    // Global Params
    private var scrollState: Int = 0
    lateinit var worldNewsList: ListView
    lateinit var worldNewsAdapter: CustomAdapter
    lateinit var EmptyStateTextView: TextView
    lateinit var loadSpin: ProgressBar
    lateinit var worldUri: Uri.Builder
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var mLoaderManager: LoaderManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //inflate the fragment with world_fragment layout
        return inflater.inflate(R.layout.world_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)

        setupNewsList()

        // URL to fetch data for World news
        worldUri = Master.getWorldUri()
        buildUriParams()

        mLoaderManager = activity!!.supportLoaderManager

        //this is to prevent loader from re-fetching the data
        if (worldNewsAdapter.count == 0 && Master.checkNet(context!!)) {
            executeLoader()
        } else {
            loadSpin.visibility = View.GONE
            EmptyStateTextView.setText(R.string.no_network)
        }

        worldNewsList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
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

                    if (webBrowserIntent.resolveActivity(activity!!.packageManager) != null) {
                        startActivity(webBrowserIntent)
                    }
                }

            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            EmptyStateTextView.text = ""

            WORLD_NEWS_LOADER += 1
            swipeRefreshLayout.isRefreshing = true

            if (Master.checkNet(context!!)) {
                executeLoader()
            } else {
                loadSpin.visibility = View.GONE
                EmptyStateTextView.setText(R.string.no_network)
            }

            destroyLoader(WORLD_NEWS_LOADER - 1)

            swipeRefreshLayout.isRefreshing = false
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
        worldUri.appendQueryParameter("api-key", Master.getAPIKey())
    }

    /**
     * Initiate News List View, Adapter and add animation
     * Set Empty View
     */
    private fun setupNewsList() {
        worldNewsList.animate().alpha(0.1f).duration = 400

        // Initiate CustomAdapter and set it for worldNewsList
        worldNewsAdapter = CustomAdapter(activity!!.applicationContext, R.layout.custom_list, java.util.ArrayList())
        worldNewsList.adapter = worldNewsAdapter

        // Set empty text view for a proper msg to user
        worldNewsList.emptyView = EmptyStateTextView
    }

    private fun initializeViews(view: View) {
        worldNewsList = view.findViewById(R.id.world_news_list)
        //user interactive views
        loadSpin = view.findViewById(R.id.load_spin)
        EmptyStateTextView = view.findViewById(R.id.empty_view)
        swipeRefreshLayout = view.findViewById(R.id.world_refresh)
    }

    /**
     * This is to prevent ListView losing the scroll position
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        try {
            outState.putInt("CURRENT_SCROLL", worldNewsList.firstVisiblePosition)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getInt("CURRENT_SCROLL")
            worldNewsList.setSelection(scrollState)
        }
    }

    //Initiate and destroy loader methods to be called after search is submitted
    private fun executeLoader() {
        mLoaderManager.initLoader(WORLD_NEWS_LOADER, null, this)
    }

    private fun destroyLoader(id: Int) {
        mLoaderManager.destroyLoader(id)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<java.util.ArrayList<NewsData>> {
        return NewsLoader(activity!!, worldUri.toString())
    }

    override fun onLoadFinished(loader: Loader<java.util.ArrayList<NewsData>>, news: java.util.ArrayList<NewsData>?) {
        EmptyStateTextView.setText(R.string.no_news)

        // Clear the adapter of previous books data
        worldNewsAdapter.clear()

        loadSpin.visibility = View.GONE

        if (news != null && !news.isEmpty()) {
            worldNewsAdapter.addAll(news)
            worldNewsList.animate().alpha(1.0f).duration = 400
            worldNewsList.setSelection(scrollState)
        }
    }

    override fun onLoaderReset(loader: Loader<java.util.ArrayList<NewsData>>) {
        worldNewsAdapter.clear()
    }
}