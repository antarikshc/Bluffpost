package com.antarikshc.bluffpost.utils

import com.antarikshc.bluffpost.models.Author
import com.antarikshc.bluffpost.utils.DataFactory.randomString
import com.antarikshc.bluffpost.utils.DataFactory.randomUuid

object AuthorFactory {

    fun randomAuthor(newsId: String = randomUuid()) = Author(
        id = randomString(),
        newsId = newsId,
        title = randomString(),
        bio = randomString()
    )

}