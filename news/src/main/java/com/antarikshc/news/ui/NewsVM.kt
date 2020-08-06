package com.antarikshc.news.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.antarikshc.news.data.repository.HomeRepository
import com.antarikshc.news.di.NewsScope
import com.antarikshc.news.models.News
import javax.inject.Inject

@NewsScope
class NewsVM @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    val news: LiveData<PagedList<News>> = repository.getPagedNewsList(viewModelScope)

    fun refresh() = repository.refreshNews()

}