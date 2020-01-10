package com.antarikshc.bluffpost.data.local

import androidx.room.*
import com.antarikshc.bluffpost.models.News
import com.antarikshc.bluffpost.models.NewsAuthorJunction
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

    @Transaction
    @Query("SELECT * from news_table ORDER BY publication_date DESC")
    fun get(): Flow<List<NewsAuthorJunction>>

}