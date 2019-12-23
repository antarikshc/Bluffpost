package com.antarikshc.bluffpost.models

import com.google.gson.annotations.SerializedName

data class Content(

    val headline: String,

    val byline: String,

    @SerializedName("thumbnail")
    val thumbnailUrl: String
)