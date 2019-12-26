package com.antarikshc.bluffpost.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class News(

    val id: String,

    @SerializedName("webTitle")
    val title: String,

    val webUrl: String,

    val apiUrl: String,

    @SerializedName("webPublicationDate")
    val publicationDate: Date,

    @SerializedName("fields")
    val content: Content,

    @SerializedName("tags")
    val authors: List<Author>

) {
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