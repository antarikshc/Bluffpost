package com.antarikshc.bluffpost.data.repository

import androidx.lifecycle.asFlow
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.antarikshc.bluffpost.data.local.AppDatabase
import com.antarikshc.bluffpost.data.local.NewsBoundaryCallback
import com.antarikshc.bluffpost.data.remote.NewsService
import com.antarikshc.bluffpost.models.Author
import com.antarikshc.bluffpost.models.News
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HomeRepository @Inject constructor(
    private val service: NewsService,
    private val db: AppDatabase
) {

    /**
     * Create PagedList for News with NewsBoundaryCallback
     */
    fun getPagedNewsList(scope: CoroutineScope): Flow<PagedList<News>> {
        val boundaryCallback = NewsBoundaryCallback(
            coroutineScope = scope,
            service = service,
            response = this@HomeRepository::updateNewsDatabase
        )

        val pagedConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(true)
            .build()

        val newsPagedList = db.newsDao().getPagedNews()
            .map { it.news.copy().apply { authors = it.authors } } // Map to News from Junction
            .toLiveData(config = pagedConfig, boundaryCallback = boundaryCallback)

        return newsPagedList.asFlow()
    }

    /**
     * Update News database
     * if PAGE = 1 then clear data and add news
     * if PAGE > 1 then append news
     */
    private fun updateNewsDatabase(page: Int, news: List<News>) {
        db.runInTransaction {
            if (page == 1) {
                db.newsDao().deleteAll()
                db.authorDao().deleteAll()
            }
            db.newsDao().insert(news)
            val authors = arrayListOf<Author>()
            news.forEach {
                authors.addAll(it.authors.map { author -> author.copy(newsId = it.id) })
            }
            db.authorDao().insert(authors)
        }
    }

}