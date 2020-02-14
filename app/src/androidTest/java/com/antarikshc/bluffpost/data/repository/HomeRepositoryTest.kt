package com.antarikshc.bluffpost.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.antarikshc.bluffpost.data.remote.FakeNewsService
import com.antarikshc.bluffpost.utils.NewsFactory
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class HomeRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: com.antarikshc.news.data.repository.HomeRepository
    private lateinit var db: AppDatabase
    private var service = FakeNewsService()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        repository =
            com.antarikshc.news.data.repository.HomeRepository(service, db)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun getPagedNewsList() = runBlocking {
        val pagedList = repository.getPagedNewsList(this).getOrAwaitValue()

        val news = db.newsDao().get().take(1).toList()
        assertEquals(true, news.isNotEmpty())
        assertEquals(true, news[0].isNotEmpty())
        assertEquals(true, pagedList.loadedCount >= 10)
    }

    @Test
    fun updateNewsDbForFirstPage() = runBlocking {
        // Given - prepopulated DB
        db.newsDao().insert(NewsFactory.randomNewsList(10))

        // When - we insert page 1 data
        val page = 1
        val news = NewsFactory.randomNewsList(10)
        repository.updateNewsDatabase(page, news)

        // Then - check only page 1 data is present in DB
        val newsFromDb = db.newsDao().getNews()
        assertEquals(true, newsFromDb.size == news.size)
        assertEquals(true, newsFromDb.containsAll(news))
    }

    @Test
    fun updateNewsDbForSecondPage() = runBlocking {
        // Given - 1st page data
        val firstPageNews = NewsFactory.randomNewsList(10)
        repository.updateNewsDatabase(1, firstPageNews)

        // When - 2nd page data is added
        val secondPageNews = NewsFactory.randomNewsList(10)
        repository.updateNewsDatabase(2, secondPageNews)

        // Then - check that 2nd page data is appended
        val newsFromDb = db.newsDao().getNews()
        assertEquals(true, newsFromDb.size == firstPageNews.size + secondPageNews.size)
        assertEquals(true, newsFromDb.containsAll(firstPageNews))
        assertEquals(true, newsFromDb.containsAll(secondPageNews))
    }
}