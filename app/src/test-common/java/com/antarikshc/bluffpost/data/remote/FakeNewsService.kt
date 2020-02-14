package com.antarikshc.bluffpost.data.remote

import com.antarikshc.bluffpost.utils.NewsFactory
import com.antarikshc.news.models.NewsResponse

class FakeNewsService : com.antarikshc.news.data.remote.NewsService {

    companion object {
        private const val COUNT = 10
    }

    override suspend fun getNews(
        format: String,
        minDate: String,
        editorPicks: Boolean,
        fields: String,
        tags: String,
        page: Int,
        key: String
    ): com.antarikshc.news.models.NewsResponse {
        val news = NewsFactory.randomNewsList(COUNT)
        return com.antarikshc.news.models.NewsResponse(
            currentPage = page,
            totalPages = 100,
            results = news
        )
    }

}