package com.antarikshc.bluffpost.data.repository

import com.antarikshc.bluffpost.data.local.AppDatabase
import com.antarikshc.bluffpost.data.remote.NewsService
import com.antarikshc.bluffpost.models.Author
import com.antarikshc.bluffpost.models.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HomeRepository @Inject constructor(
    private val service: NewsService,
    private val db: AppDatabase
) {

    fun getNews(): Flow<List<News>> {
        return db.newsDao().get()
            .map {
                // Map NewsAuthorJunction back to News
                // copy authors from junction and populate News.authors
                it.map { junction -> junction.news.copy(authors = junction.authors) }
            }
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
            .catch { it.printStackTrace() }
    }

    suspend fun fetchNews() {
        withContext(Dispatchers.IO) {
            // Fetch news
            val news = service.getNews()

            // Store results in DB
            db.runInTransaction {
                db.newsDao().insert(news.results)
                val authors = arrayListOf<Author>()
                news.results.forEach {
                    authors.addAll(it.authors.map { author -> author.copy(newsId = it.id) })
                }
                db.authorDao().insert(authors)
            }
        }
    }

}