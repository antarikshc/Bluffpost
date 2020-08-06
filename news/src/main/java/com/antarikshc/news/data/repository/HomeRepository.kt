package com.antarikshc.news.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.antarikshc.news.data.local.NewsBoundaryCallback
import com.antarikshc.news.data.local.NewsDatabase
import com.antarikshc.news.data.remote.NewsService
import com.antarikshc.news.di.NewsScope
import com.antarikshc.news.models.Author
import com.antarikshc.news.models.News
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@NewsScope
@ExperimentalCoroutinesApi
class HomeRepository @Inject constructor(
    private val service: NewsService,
    private val db: NewsDatabase
) {

    private var boundaryCallback: NewsBoundaryCallback? = null

    fun refreshNews() {
        boundaryCallback?.refreshItems()
    }

    /**
     * Create PagedList for [News] with [NewsBoundaryCallback], return LiveData of [News]
     */
    fun getPagedNewsList(scope: CoroutineScope): LiveData<PagedList<News>> {
        boundaryCallback = NewsBoundaryCallback(
            coroutineScope = scope,
            service = service,
            response = this@HomeRepository::updateNewsDatabase
        )

        val pagedConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(true)
            .build()

        return db.newsDao().getPagedNews()
            .map { it.news.copy().apply { authors = it.authors } } // Map to News from Junction
            .toLiveData<Int?, News>(config = pagedConfig, boundaryCallback = boundaryCallback)
    }

    /**
     * Update News database
     * if PAGE = 1 then clear data and add news
     * if PAGE > 1 then append news
     */
    fun updateNewsDatabase(page: Int, news: List<News>) {
        db.runInTransaction {
            if (page == 1) {
                db.newsDao().deleteAll()
                db.authorDao().deleteAll()
            }
            db.newsDao().insert(news)
            val authors = mutableListOf<Author>()
            news.forEach {
                authors.addAll(it.authors.map { author -> author.copy(newsId = it.id) })
            }
            db.authorDao().insert(authors)
        }
    }

}