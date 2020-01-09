package com.antarikshc.bluffpost.data.repository

import com.antarikshc.bluffpost.data.local.AppDatabase
import com.antarikshc.bluffpost.data.remote.NewsService
import com.antarikshc.bluffpost.models.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HomeRepository @Inject constructor(
    private val service: NewsService,
    private val db: AppDatabase
) {

    fun getNews(): Flow<NewsResponse> {
        return flow {
            emit(service.getNews())
        }
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
            .catch { it.printStackTrace() }
    }

}