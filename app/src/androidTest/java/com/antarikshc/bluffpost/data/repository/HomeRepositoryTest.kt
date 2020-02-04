package com.antarikshc.bluffpost.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.antarikshc.bluffpost.data.local.AppDatabase
import com.antarikshc.bluffpost.data.remote.FakeNewsService
import com.antarikshc.bluffpost.utils.getOrAwaitValue
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
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

    private lateinit var repository: HomeRepository
    private lateinit var db: AppDatabase
    private var service = FakeNewsService()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        repository = HomeRepository(service, db)
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
}