package com.antarikshc.bluffpost.models

import com.google.gson.annotations.SerializedName

data class NewsResponse(

    val currentPage: Int,

    @SerializedName("pages")
    val totalPages: Int,

    val results: List<News>

)