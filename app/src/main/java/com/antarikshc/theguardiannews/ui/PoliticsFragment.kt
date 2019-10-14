package com.antarikshc.theguardiannews.ui

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
import com.antarikshc.theguardiannews.util.openBrowser
import kotlinx.android.synthetic.main.politics_fragment.*

class PoliticsFragment : Fragment(), LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    // Global params
    private var scrollState = 0
    private lateinit var politicsNewsAdapter: CustomAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.politics_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNewsList()

        // URL to fetch data for Politics News
        buildUriParams()

        //this is to prevent loader from re-fetching the data
        if (politicsNewsAdapter.count == 0 && Master.checkNet(context)) {
            executeLoader()
        } else {
            load_spin.visibility = View.GONE
            empty_view.setText(R.string.no_network)
        }

        politics_news_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            activity?.openBrowser(politicsNewsAdapter.getItem(position))
        }

        politics_refresh.setOnRefreshListener {
            empty_view.text = ""

            POLITICS_NEWS_LOADER += 1
            politics_refresh.isRefreshing = true

            if (Master.checkNet(context)) {
                executeLoader()
            } else {
                load_spin.visibility = View.GONE
                empty_view.setText(R.string.no_network)
            }

            destroyLoader(POLITICS_NEWS_LOADER - 1)

            politics_refresh.isRefreshing = false
        }

    }

    /**
     * Add URL params to call API
     */
    private fun buildUriParams() {
        politicsUri.appendQueryParameter("q", "politics")
        politicsUri.appendQueryParameter("format", "json")
        politicsUri.appendQueryParameter("page-size", "8")
        politicsUri.appendQueryParameter("from-date", "2018-01-01")
        politicsUri.appendQueryParameter("show-fields", "thumbnail,headline,byline")
        //politicsUri.appendQueryParameter("show-tags", "contributor");  -not getting results for politics with this tag
        politicsUri.appendQueryParameter("api-key", Master.apiKey)
    }

    /**
     * Initiate News List View, Adapter and add animation
     * Set Empty View
     */
    private fun setupNewsList() {
        politics_news_list.animate().alpha(0.1f).duration = 400
        // Initiate CustomAdapter and set it for worldNewsList
        activity?.run {
            politicsNewsAdapter = CustomAdapter(applicationContext, ArrayList())
        }
        politics_news_list.adapter = politicsNewsAdapter

        //set empty text view for a proper msg to user
        politics_news_list.emptyView = empty_view
    }

    /**
     * This is to prevent ListView losing the scroll position
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        try {
            outState.putInt("CURRENT_SCROLL", politics_news_list.firstVisiblePosition)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getInt("CURRENT_SCROLL")
            politics_news_list.setSelection(scrollState)
        }
    }

    // Initiate and destroy loader methods to be called after search is submitted
    private fun executeLoader() {
        loaderManager.initLoader(POLITICS_NEWS_LOADER, null, this)
    }

    private fun destroyLoader(id: Int) {
        loaderManager.destroyLoader(id)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): androidx.loader.content.Loader<ArrayList<NewsData>> {
        return NewsLoader(activity!!, politicsUri.toString())
    }

    override fun onLoadFinished(loader: androidx.loader.content.Loader<ArrayList<NewsData>>, news: ArrayList<NewsData>?) {
        empty_view.setText(R.string.no_news)

        // Clear the adapter of previous books data
        politicsNewsAdapter.clear()

        load_spin.visibility = View.GONE

        if (news != null && news.isNotEmpty()) {
            politicsNewsAdapter.addAll(news)
            politics_news_list.animate().alpha(1.0f).duration = 400
            politics_news_list.setSelection(scrollState)
        }
    }

    override fun onLoaderReset(loader: androidx.loader.content.Loader<ArrayList<NewsData>>) {
        politicsNewsAdapter.clear()
    }

    companion object {

        //Books loaded ID, default = 1 currently using single Loader
        private var POLITICS_NEWS_LOADER = 25
        private var politicsUri = Master.searchUri
    }

}