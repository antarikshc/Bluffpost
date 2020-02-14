package com.antarikshc.bluffpost.utils

import com.antarikshc.bluffpost.utils.DataFactory.randomString
import com.antarikshc.bluffpost.utils.DataFactory.randomUuid
import com.antarikshc.news.models.Author

object AuthorFactory {

    fun randomAuthor(newsId: String = randomUuid()) =
        com.antarikshc.news.models.Author(
            id = randomString(),
            newsId = newsId,
            title = randomString(),
            bio = randomString()
        )

}