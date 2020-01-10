package com.antarikshc.bluffpost.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.antarikshc.bluffpost.data.repository.HomeRepository
import com.antarikshc.bluffpost.models.News
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeVM @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    // Fetch news from network on Initialization
    init {
        viewModelScope.launch { repository.fetchNews() }
    }

    // Return Stream of News from DB
    val news: LiveData<List<News>> = repository.getNews().asLiveData()

}