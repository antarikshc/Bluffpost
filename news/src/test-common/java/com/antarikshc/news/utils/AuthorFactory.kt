package com.antarikshc.news.utils

import com.antarikshc.news.models.Author
import com.antarikshc.news.utils.DataFactory.randomString
import com.antarikshc.news.utils.DataFactory.randomUuid

object AuthorFactory {

    fun randomAuthor(newsId: String = randomUuid()) =
        Author(
            id = randomString(),
            newsId = newsId,
            title = randomString(),
            bio = randomString()
        )

}