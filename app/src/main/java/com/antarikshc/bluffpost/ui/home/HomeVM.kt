package com.antarikshc.bluffpost.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.antarikshc.bluffpost.data.repository.HomeRepository
import com.antarikshc.bluffpost.models.NewsResponse
import javax.inject.Inject

class HomeVM @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    val news: LiveData<NewsResponse> = repository.getNews().asLiveData()

}