package com.antarikshc.theguardiannews.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import com.antarikshc.theguardiannews.R
import com.antarikshc.theguardiannews.datasource.NewsLoader
import com.antarikshc.theguardiannews.model.NewsData
import com.antarikshc.theguardiannews.util.Master

class SearchActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    // Global params
    private var SEARCH_NEWS_URL: String? = null

    lateinit var searchIntent: Intent
    private var scrollState: Int = 0
    lateinit var toolbar: Toolbar
    lateinit var searchNewsList: ListView
    lateinit var searchNewsAdapter: CustomAdapter
    lateinit var EmptyStateTextView: TextView
    lateinit var loadSpin: ProgressBar
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var loaderManager: LoaderManager

    //we are using different loaders for each tab
    private var SEARCH_NEWS_LOADER = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbar = findViewById(R.id.search_activity_toolbar)

        swipeRefreshLayout = findViewById(R.id.search_refresh)
        searchNewsList = findViewById(R.id.search_news_list)

        //user interactive views
        loadSpin = findViewById(R.id.load_spin)
        EmptyStateTextView = findViewById(R.id.empty_view)

        searchIntent = intent

        //set the title as the search query on toolbar
        var title = searchIntent.getStringExtra("title")
        title = title.substring(0, 1).toUpperCase() + title.substring(1)
        toolbar.title = title

        //set it as action bar to retrieve it back
        setSupportActionBar(toolbar)

        //Add back button in Toolbar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        SEARCH_NEWS_URL = searchIntent.getStringExtra("url")

        loadSpin.visibility = View.VISIBLE
        searchNewsList.animate().alpha(0.1f).duration = 400

        //initiate CustomAdapter and set it for worldNewsList
        searchNewsAdapter = CustomAdapter(applicationContext, R.layout.custom_list, java.util.ArrayList())
        searchNewsList.adapter = searchNewsAdapter

        //set empty text view for a proper msg to user
        searchNewsList.emptyView = EmptyStateTextView

        loaderManager = supportLoaderManager

        if (Master.checkNet(this@SearchActivity) && searchNewsAdapter.count == 0) {
            executeLoader()
        } else {
            loadSpin.visibility = View.GONE
            EmptyStateTextView.setText(R.string.no_network)
        }

        searchNewsList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val currentData = searchNewsAdapter.getItem(position)

            if (currentData != null) {

                try {

                    //Explicit Intent
                    val webActivityIntent = Intent(applicationContext, WebviewActivity::class.java)
                    webActivityIntent.putExtra("url", currentData.webUrl)
                    startActivity(webActivityIntent)

                } catch (e: Exception) {

                    //Implicit Intent
                    val webUri = Uri.parse(currentData.webUrl)
                    val webBrowserIntent = Intent(Intent.ACTION_VIEW, webUri)

                    if (webBrowserIntent.resolveActivity(packageManager) != null) {
                        startActivity(webBrowserIntent)
                    }
                }

            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            EmptyStateTextView.text = ""

            SEARCH_NEWS_LOADER += 1
            swipeRefreshLayout.isRefreshing = true

            if (Master.checkNet(this@SearchActivity)) {
                executeLoader()
            } else {
                loadSpin.visibility = View.GONE
                EmptyStateTextView.setText(R.string.no_network)
            }

            destroyLoader(SEARCH_NEWS_LOADER - 1)

            swipeRefreshLayout.isRefreshing = false
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //This is to prevent listview losing the scroll position
    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        try {
            outState.putInt("CURRENT_SCROLL", searchNewsList.firstVisiblePosition)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getInt("CURRENT_SCROLL")
            searchNewsList.setSelection(scrollState)
        }
    }

    //Initiate and destroy loader methods to be called after search is submitted
    private fun executeLoader() {
        loaderManager.initLoader(SEARCH_NEWS_LOADER, null, this)
    }

    private fun destroyLoader(id: Int) {
        loaderManager.destroyLoader(id)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<java.util.ArrayList<NewsData>> {
        return NewsLoader(this, SEARCH_NEWS_URL!!)
    }

    override fun onLoadFinished(loader: Loader<java.util.ArrayList<NewsData>>, news: java.util.ArrayList<NewsData>?) {
        EmptyStateTextView.setText(R.string.no_news)

        // Clear the adapter of previous books data
        searchNewsAdapter.clear()

        loadSpin.visibility = View.GONE

        if (news != null && !news.isEmpty()) {
            searchNewsAdapter.addAll(news)
            searchNewsList.animate().alpha(1.0f).duration = 400
            searchNewsList.setSelection(scrollState)
        }
    }

    override fun onLoaderReset(loader: Loader<java.util.ArrayList<NewsData>>) {
        searchNewsAdapter.clear()
    }
}