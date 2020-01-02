package com.antarikshc.bluffpost.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.antarikshc.bluffpost.data.remote.NewsService
import com.antarikshc.bluffpost.models.NewsResponse
import javax.inject.Inject

class HomeVM @Inject constructor(private val service: NewsService) : ViewModel() {

    val news: LiveData<NewsResponse> = liveData { emit(service.getNews()) }

}