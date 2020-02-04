package com.antarikshc.bluffpost.data.remote

import com.antarikshc.bluffpost.models.NewsResponse
import com.antarikshc.bluffpost.utils.NewsFactory

class FakeNewsService : NewsService {

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
    ): NewsResponse {
        val news = NewsFactory.randomNewsList(COUNT)
        return NewsResponse(
            currentPage = page,
            totalPages = 100,
            results = news
        )
    }

}