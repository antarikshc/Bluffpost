package com.antarikshc.bluffpost.data.local

import androidx.room.*
import com.antarikshc.bluffpost.models.Author
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(author: Author)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(authors: List<Author>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(author: Author)

    @Query("SELECT * FROM authors_table")
    fun get(): Flow<List<Author>>

    @Query("DELETE from authors_table")
    fun deleteAll()

}