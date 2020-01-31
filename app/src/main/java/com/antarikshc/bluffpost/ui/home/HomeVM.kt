package com.antarikshc.bluffpost.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.antarikshc.bluffpost.data.repository.HomeRepository
import com.antarikshc.bluffpost.models.News
import javax.inject.Inject

class HomeVM @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    val news: LiveData<PagedList<News>> = repository.getPagedNewsList(viewModelScope)

    fun refresh() = repository.refreshNews()

}