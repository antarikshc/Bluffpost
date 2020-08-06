package com.antarikshc.news.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.antarikshc.news.utils.NewsFactory
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
open class NewsDaoTest {

    private lateinit var db: NewsDatabase
    private lateinit var newsDao: NewsDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).build()
        newsDao = db.newsDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun insertNews() = runBlocking {
        val news = NewsFactory.randomNews()

        newsDao.insert(news)

        val newsFromDb = newsDao.get(news.id)
        assertEquals(news.id, newsFromDb?.id)
    }

    @Test
    fun insertNewsList() = runBlocking {
        val news = NewsFactory.randomNewsList(5)

        newsDao.insert(news)

        val newsFromDb = newsDao.getNews()
        assertEquals(true, newsFromDb.containsAll(news))
    }

    @Test
    fun deleteAllNews() = runBlocking {
        val news = NewsFactory.randomNewsList(5)
        newsDao.insert(news)

        newsDao.deleteAll()

        val newsFromDb = newsDao.getNews()
        assertEquals(true, newsFromDb.isEmpty())
    }
}