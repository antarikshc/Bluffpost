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
import com.antarikshc.theguardiannews.util.Master.searchUri
import kotlinx.android.synthetic.main.sports_fragment.*

class SportsFragment : Fragment(), LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    // Global params
    private var scrollState = 0
    private lateinit var sportsNewsAdapter: CustomAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.sports_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNewsList()

        // URL to fetch data for Sports News
        buildUriParams()

        //this is to prevent loader from re-fetching the data
        if (sportsNewsAdapter.count == 0 && Master.checkNet(context)) {
            executeLoader()
        } else {
            load_spin.visibility = View.GONE
            empty_view.setText(R.string.no_network)
        }

        sports_news_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
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
                    activity?.run {
                        if (webBrowserIntent.resolveActivity(packageManager) != null) {
                            startActivity(webBrowserIntent)
                        }
                    }
                }
            }
        }

        sports_refresh.setOnRefreshListener {
            empty_view.text = ""

            SPORTS_NEWS_LOADER += 1
            sports_refresh.isRefreshing = true

            if (Master.checkNet(context)) {
                executeLoader()
            } else {
                load_spin.visibility = View.GONE
                empty_view.setText(R.string.no_network)
            }

            destroyLoader(SPORTS_NEWS_LOADER - 1)

            sports_refresh.isRefreshing = false
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
        sportsUri.appendQueryParameter("api-key", Master.apiKey)
    }

    /**
     * Initiate News List View, Adapter and add animation
     * Set Empty View
     */
    private fun setupNewsList() {
        sports_news_list.animate().alpha(0.1f).duration = 400

        //initiate CustomAdapter and set it for worldNewsList
        sportsNewsAdapter = CustomAdapter(activity!!.applicationContext, ArrayList())
        sports_news_list.adapter = sportsNewsAdapter

        //set empty text view for a proper msg to user
        sports_news_list.emptyView = empty_view
    }

    /**
     * This is to prevent ListView losing the scroll position
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        try {
            outState.putInt("CURRENT_SCROLL", sports_news_list.firstVisiblePosition)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getInt("CURRENT_SCROLL")
            sports_news_list.setSelection(scrollState)
        }
    }

    // Initiate and destroy loader methods to be called after search is submitted
    private fun executeLoader() {
        loaderManager.initLoader(SPORTS_NEWS_LOADER, null, this)
    }

    private fun destroyLoader(id: Int) {
        loaderManager.destroyLoader(id)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): androidx.loader.content.Loader<ArrayList<NewsData>> {
        return NewsLoader(activity!!, sportsUri.toString())
    }

    override fun onLoadFinished(loader: androidx.loader.content.Loader<ArrayList<NewsData>>, news: ArrayList<NewsData>?) {
        empty_view.setText(R.string.no_news)

        // Clear the adapter of previous books data
        sportsNewsAdapter.clear()

        load_spin.visibility = View.GONE

        if (news != null && news.isNotEmpty()) {
            sportsNewsAdapter.addAll(news)
            sports_news_list.animate().alpha(1.0f).duration = 400
            sports_news_list.setSelection(scrollState)
        }
    }

    override fun onLoaderReset(loader: androidx.loader.content.Loader<ArrayList<NewsData>>) {
        sportsNewsAdapter.clear()
    }

    companion object {

        // Books loaded ID, default = 1 currently using single Loader
        private var SPORTS_NEWS_LOADER = 50
        private val sportsUri = searchUri
    }
}