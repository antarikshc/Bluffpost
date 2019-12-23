package com.antarikshc.bluffpost.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.antarikshc.bluffpost.data.remote.NewsService
import com.antarikshc.bluffpost.models.NewsResponse
import com.antarikshc.bluffpost.utils.NewsResponseDeserializer
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeVM : ViewModel() {

    private val service: NewsService by lazy { provideNewsService() }

    val news: LiveData<NewsResponse> = liveData { emit(service.getNews()) }

    private fun provideNewsService(): NewsService {
        val gson = GsonBuilder()
            .registerTypeAdapter(NewsResponse::class.java, NewsResponseDeserializer())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()

        return Retrofit.Builder()
            .baseUrl("https://content.guardianapis.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NewsService::class.java)
    }

}