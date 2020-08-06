package com.antarikshc.news.data.remote

import com.antarikshc.bluffpost.BuildConfig
import com.antarikshc.news.models.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("world")
    suspend fun getNews(
        @Query("format") format: String = "json",
        @Query("from-date") minDate: String = "2018-01-01",
        @Query("show-editors-picks") editorPicks: Boolean = true,
        @Query("show-fields") fields: String = "thumbnail,headline,byline",
        @Query("show-tags") tags: String = "contributor",
        @Query("page") page: Int = 1,
        @Query("api-key") key: String = BuildConfig.API_KEY
    ): NewsResponse

}