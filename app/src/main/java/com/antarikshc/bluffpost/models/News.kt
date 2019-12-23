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
)