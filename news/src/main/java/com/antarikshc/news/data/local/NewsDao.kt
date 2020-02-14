package com.antarikshc.news.data.local

import androidx.paging.DataSource
import androidx.room.*
import com.antarikshc.news.models.News
import com.antarikshc.news.models.NewsAuthorJunction
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: News)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: List<News>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(news: News)

    @Query("SELECT * from news_table WHERE id = :id")
    suspend fun get(id: String): News?

    @Query("SELECT * from news_table")
    suspend fun getNews(): List<News>

    @Transaction
    @Query("SELECT * from news_table ORDER BY publication_date DESC")
    fun get(): Flow<List<NewsAuthorJunction>>

    @Transaction
    @Query("SELECT * from news_table ORDER BY publication_date DESC")
    fun getPagedNews(): DataSource.Factory<Int, NewsAuthorJunction>

    @Query("DELETE from news_table")
    fun deleteAll()

}