package com.antarikshc.news.utils

import com.antarikshc.news.models.News
import com.antarikshc.news.models.NewsAuthorJunction
import com.antarikshc.news.utils.DataFactory.randomLong
import com.antarikshc.news.utils.DataFactory.randomString
import com.antarikshc.news.utils.DataFactory.randomUuid
import java.util.*

object NewsFactory {

    fun randomNews() = News(
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

    fun randomNewsList(count: Int = 5): List<News> {
        val list = mutableListOf<News>()
        repeat(count) {
            list.add(randomNews())
        }
        return list
    }

    fun randomNewsAuthorJunction(count: Int = 5): List<NewsAuthorJunction> {
        val list = mutableListOf<NewsAuthorJunction>()
        repeat(count) {
            val news = randomNews()
            val author = AuthorFactory.randomAuthor(news.id)
            list += NewsAuthorJunction(
                news,
                listOf(author)
            )
        }
        return list
    }

}