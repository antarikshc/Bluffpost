package com.antarikshc.bluffpost.models

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "news_table")
data class News(

    @PrimaryKey(autoGenerate = false)
    var id: String = "",

    @SerializedName("webTitle")
    var title: String = "",

    @ColumnInfo(name = "web_url")
    var webUrl: String = "",

    @ColumnInfo(name = "api_url")
    var apiUrl: String = "",

    @ColumnInfo(name = "publication_date")
    @SerializedName("webPublicationDate")
    var publicationDate: Date? = null,

    @Embedded(prefix = "content_")
    @SerializedName("fields")
    var content: Content? = null,

    @Ignore     // Field will be populated with Junction
    @SerializedName("tags")
    var authors: List<Author> = listOf()

) {

    fun getAuthor(): String? = if (!authors.isNullOrEmpty()) authors[0].title else null

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        other as News

        if (id != other.id) return false
        if (title != other.title) return false
        if (webUrl != other.webUrl) return false
        if (publicationDate != other.publicationDate) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + webUrl.hashCode()
        result = 31 * result + apiUrl.hashCode()
        result = 31 * result + publicationDate.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + authors.hashCode()
        return result
    }
}