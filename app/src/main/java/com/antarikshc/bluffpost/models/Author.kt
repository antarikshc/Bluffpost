package com.antarikshc.bluffpost.models

import com.google.gson.annotations.SerializedName

data class Author(

    @SerializedName("webTitle")
    val title: String,

    val bio: String
)