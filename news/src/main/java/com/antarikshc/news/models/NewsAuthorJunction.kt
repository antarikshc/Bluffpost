package com.antarikshc.news.models

import androidx.room.Embedded
import androidx.room.Relation

data class NewsAuthorJunction(

    @Embedded
    val news: News,

    @Relation(parentColumn = "id", entityColumn = "news_id", entity = Author::class)
    val authors: List<Author>

)