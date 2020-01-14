package com.antarikshc.bluffpost.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "authors_table")
data class Author(

    @PrimaryKey(autoGenerate = false)
    var id: String = "",

    @ColumnInfo(name = "news_id")
    val newsId: String = "",

    @SerializedName("webTitle")
    val title: String,

    val bio: String?
)