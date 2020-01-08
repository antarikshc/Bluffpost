package com.antarikshc.bluffpost.di

import android.app.Application
import com.antarikshc.bluffpost.data.local.AppDatabase
import com.antarikshc.bluffpost.data.remote.NewsService
import com.antarikshc.bluffpost.models.NewsResponse
import com.antarikshc.bluffpost.utils.NewsResponseDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object AppModule {

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(NewsResponse::class.java, NewsResponseDeserializer())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
    }

    @Provides
    fun provideNewsService(gson: Gson): NewsService {
        return Retrofit.Builder()
            .baseUrl("https://content.guardianapis.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NewsService::class.java)
    }

    @Provides
    fun provideDatabase(application: Application): AppDatabase {
        return AppDatabase.build(application)
    }

}