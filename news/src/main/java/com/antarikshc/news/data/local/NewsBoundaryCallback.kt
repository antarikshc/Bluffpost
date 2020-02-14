package com.antarikshc.news.data.local

import android.util.Log
import androidx.paging.PagedList
import com.antarikshc.news.data.remote.NewsService
import com.antarikshc.news.models.News
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewsBoundaryCallback(
    private val coroutineScope: CoroutineScope,
    private val service: NewsService,
    private val response: (Int, List<News>) -> Unit
) : PagedList.BoundaryCallback<News>() {

    companion object {
        private val TAG = NewsBoundaryCallback::class.java.simpleName
    }

    private var currentResponse =
        com.antarikshc.news.models.NewsResponse(0, 0, listOf())
    private var job: Job? = null

    init {
        // Load First page to get latest data
        refreshItems()
    }

    fun refreshItems() {
        Log.d(TAG, "onRefreshItems")
        if (job != null && job?.isActive != false) job?.cancel()
        job = coroutineScope.launch(Dispatchers.IO) {
            currentResponse = service.getNews(page = 1)
            response(currentResponse.currentPage, currentResponse.results)
        }
    }

    override fun onZeroItemsLoaded() {
        Log.d(TAG, "onZeroItemsLoaded")
        if (job == null || job?.isActive == false) {
            job = coroutineScope.launch(Dispatchers.IO) {
                currentResponse = service.getNews(page = 1)
                response(currentResponse.currentPage, currentResponse.results)
            }
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: News) {
        // Cannot use this because we need fresh data on init
        // API only allows pagination by Page number and not ID
        Log.d(TAG, "onItemAtFrontLoaded ${itemAtFront.title}")
    }

    override fun onItemAtEndLoaded(itemAtEnd: News) {
        Log.d(TAG, "onItemAtEndLoaded ${itemAtEnd.title}")
        if (job == null || job?.isActive == false) {
            job = coroutineScope.launch(Dispatchers.IO) {
                Log.d(TAG, "Fetching news at page: ${currentResponse.currentPage + 1}")
                currentResponse = service.getNews(page = currentResponse.currentPage + 1)
                response(currentResponse.currentPage, currentResponse.results)
            }
        }
    }
}