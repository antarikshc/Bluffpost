package com.antarikshc.bluffpost.utils

import com.antarikshc.bluffpost.models.Content
import com.antarikshc.bluffpost.models.News
import com.antarikshc.bluffpost.models.NewsAuthorJunction
import com.antarikshc.bluffpost.utils.DataFactory.randomLong
import com.antarikshc.bluffpost.utils.DataFactory.randomString
import com.antarikshc.bluffpost.utils.DataFactory.randomUuid
import java.util.*

object NewsFactory {

    fun randomNews() = News(
        id = randomUuid(),
        title = randomString(),
        apiUrl = randomString(),
        webUrl = randomString(),
        publicationDate = Date(randomLong(10000, 170000000)),
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

    fun randomNewsAuthorJunction(count: Int = 5): List<NewsAuthorJunction> {
        val list = mutableListOf<NewsAuthorJunction>()
        repeat(count) {
            val news = randomNews()
            val author = AuthorFactory.randomAuthor(news.id)
            list += NewsAuthorJunction(news, listOf(author))
        }
        return list
    }

}