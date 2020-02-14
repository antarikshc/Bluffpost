package com.antarikshc.bluffpost.utils

import com.antarikshc.bluffpost.utils.DataFactory.randomLong
import com.antarikshc.bluffpost.utils.DataFactory.randomString
import com.antarikshc.bluffpost.utils.DataFactory.randomUuid
import com.antarikshc.news.models.Content
import com.antarikshc.news.models.News
import com.antarikshc.news.models.NewsAuthorJunction
import java.util.*

object NewsFactory {

    fun randomNews() = com.antarikshc.news.models.News(
        id = randomUuid(),
        title = randomString(),
        apiUrl = randomString(),
        webUrl = randomString(),
        publicationDate = Date(randomLong(10000, 170000000)),
        content = com.antarikshc.news.models.Content(
            headline = randomString(),
            byline = randomString(),
            thumbnailUrl = randomString()
        )
    )

    fun randomNewsList(count: Int = 5): List<com.antarikshc.news.models.News> {
        val list = mutableListOf<com.antarikshc.news.models.News>()
        repeat(count) {
            list.add(randomNews())
        }
        return list
    }

    fun randomNewsAuthorJunction(count: Int = 5): List<com.antarikshc.news.models.NewsAuthorJunction> {
        val list = mutableListOf<com.antarikshc.news.models.NewsAuthorJunction>()
        repeat(count) {
            val news = randomNews()
            val author = AuthorFactory.randomAuthor(news.id)
            list += com.antarikshc.news.models.NewsAuthorJunction(
                news,
                listOf(author)
            )
        }
        return list
    }

}