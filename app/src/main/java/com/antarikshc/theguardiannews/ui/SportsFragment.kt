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

class SportsFragment : Fragment(), LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {


    // Books loaded ID, default = 1 currently using single Loader
    private var SPORTS_NEWS_LOADER = 50

    // Global params
    private var scrollState: Int = 0
    lateinit var sportsNewsList: ListView
    lateinit var sportsNewsAdapter: CustomAdapter
    lateinit var EmptyStateTextView: TextView
    lateinit var loadSpin: ProgressBar
    lateinit var sportsUri: Uri.Builder
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var mLoaderManager: LoaderManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sports_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)

        setupNewsList()

        // URL to fetch data for Sports News
        sportsUri = Master.getSearchUri()
        buildUriParams()

        mLoaderManager = activity!!.supportLoaderManager

        //this is to prevent loader from re-fetching the data
        if (sportsNewsAdapter.count == 0 && Master.checkNet(context!!)) {
            executeLoader()
        } else {
            loadSpin.visibility = View.GONE
            EmptyStateTextView.setText(R.string.no_network)
        }

        sportsNewsList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val currentData = sportsNewsAdapter.getItem(position)

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

            SPORTS_NEWS_LOADER += 1
            swipeRefreshLayout.isRefreshing = true

            if (Master.checkNet(context!!)) {
                executeLoader()
            } else {
                loadSpin.visibility = View.GONE
                EmptyStateTextView.setText(R.string.no_network)
            }

            destroyLoader(SPORTS_NEWS_LOADER - 1)

            swipeRefreshLayout.isRefreshing = false
        }

    }

    /**
     * Add URL params to call API
     */
    private fun buildUriParams() {
        sportsUri.appendQueryParameter("q", "sports")
        sportsUri.appendQueryParameter("format", "json")
        sportsUri.appendQueryParameter("page-size", "8")
        sportsUri.appendQueryParameter("from-date", "2018-01-01")
        sportsUri.appendQueryParameter("show-fields", "thumbnail,headline,byline")
        sportsUri.appendQueryParameter("show-tags", "contributor")
        sportsUri.appendQueryParameter("api-key", Master.getAPIKey())
    }

    /**
     * Initiate News List View, Adapter and add animation
     * Set Empty View
     */
    private fun setupNewsList() {
        sportsNewsList.animate().alpha(0.1f).duration = 400

        //initiate CustomAdapter and set it for worldNewsList
        sportsNewsAdapter = CustomAdapter(activity!!.applicationContext, R.layout.custom_list, java.util.ArrayList())
        sportsNewsList.adapter = sportsNewsAdapter

        //set empty text view for a proper msg to user
        sportsNewsList.emptyView = EmptyStateTextView
    }

    private fun initializeViews(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.sports_refresh)
        sportsNewsList = view.findViewById(R.id.sports_news_list)

        //user interactive views
        loadSpin = view.findViewById(R.id.load_spin)
        EmptyStateTextView = view.findViewById(R.id.empty_view)
    }

    /**
     * This is to prevent ListView losing the scroll position
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        try {
            outState.putInt("CURRENT_SCROLL", sportsNewsList.firstVisiblePosition)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getInt("CURRENT_SCROLL")
            sportsNewsList.setSelection(scrollState)
        }
    }

    // Initiate and destroy loader methods to be called after search is submitted
    private fun executeLoader() {
        mLoaderManager.initLoader(SPORTS_NEWS_LOADER, null, this)
    }

    private fun destroyLoader(id: Int) {
        mLoaderManager.destroyLoader(id)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<java.util.ArrayList<NewsData>> {
        return NewsLoader(activity!!, sportsUri.toString())
    }

    override fun onLoadFinished(loader: Loader<java.util.ArrayList<NewsData>>, news: java.util.ArrayList<NewsData>?) {
        EmptyStateTextView.setText(R.string.no_news)

        // Clear the adapter of previous books data
        sportsNewsAdapter.clear()

        loadSpin.visibility = View.GONE

        if (news != null && !news.isEmpty()) {
            sportsNewsAdapter.addAll(news)
            sportsNewsList.animate().alpha(1.0f).duration = 400
            sportsNewsList.setSelection(scrollState)
        }
    }

    override fun onLoaderReset(loader: Loader<java.util.ArrayList<NewsData>>) {
        sportsNewsAdapter.clear()
    }
}