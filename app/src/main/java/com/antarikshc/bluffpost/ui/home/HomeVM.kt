package com.antarikshc.bluffpost.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.antarikshc.bluffpost.data.repository.HomeRepository
import com.antarikshc.bluffpost.models.News
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class HomeVM @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _news = MutableLiveData<PagedList<News>>()
    val news: LiveData<PagedList<News>> = _news

    init {
        // Fetch paged news list from network on Initialization
        repository.getPagedNewsList(viewModelScope).onEach { _news.postValue(it) }
            .launchIn(viewModelScope)
    }

}