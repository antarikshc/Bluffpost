package com.antarikshc.bluffpost.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.antarikshc.bluffpost.models.Author
import com.antarikshc.bluffpost.utils.NewsFactory
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
open class NewsAuthorJunctionDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var newsDao: NewsDao
    private lateinit var authorDao: AuthorDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        newsDao = db.newsDao()
        authorDao = db.authorDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun insertNewsAuthorJunctionList() = runBlocking {
        val junction = NewsFactory.randomNewsAuthorJunction()

        newsDao.insert(junction.map { it.news })
        val authors = mutableListOf<Author>()
        junction.forEach { authors.addAll(it.authors) }
        authorDao.insert(authors)

        val junctionFromDb = newsDao.get().take(1).toList()

        assertEquals(true, junctionFromDb.isNotEmpty())
        assertEquals(true, junctionFromDb[0].isNotEmpty())
    }


    @Test
    fun getSortedNewsAuthorJunctionList() = runBlocking {
        val junction = NewsFactory.randomNewsAuthorJunction()

        newsDao.insert(junction.map { it.news })
        val authors = mutableListOf<Author>()
        junction.forEach { authors.addAll(it.authors) }
        authorDao.insert(authors)
        val junctionFromDb = newsDao.get().take(1).toList()

        var validation = true

        // Test for Sorting
        if (junctionFromDb.isNotEmpty()) {
            junctionFromDb[0].asSequence().zipWithNext { old, new ->
                if (old.news.publicationDate!!.time < new.news.publicationDate!!.time) validation =
                    false
            }.all { true }
        } else validation = false

        assertEquals(true, validation)
    }

}