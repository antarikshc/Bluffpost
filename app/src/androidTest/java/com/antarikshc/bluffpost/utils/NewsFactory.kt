package com.antarikshc.bluffpost.utils

import com.antarikshc.bluffpost.models.Content
import com.antarikshc.bluffpost.models.News
import com.antarikshc.bluffpost.utils.testing.DataFactory.randomUuid
import com.antarikshc.bluffpost.utils.testing.DataFactory.randomString
import java.util.*

object NewsFactory {

    fun randomNews() = News(
        id = randomUuid(),
        title = randomString(),
        apiUrl = randomString(),
        webUrl = randomString(),
        publicationDate = Date(),
        content = Content(
            headline = randomString(),
            byline = randomString(),
            thumbnailUrl = randomString()
        )
    )

    fun randomNewsList(count: Int = 5): List<News> {
        val list = mutableListOf<News>()
        repeat(count) {
            list.add(randomNews())
        }
        return list
    }

}