package com.antarikshc.theguardiannews.model

import android.graphics.Bitmap

data class NewsData(
        var title: String?,
        var author: String?,
        var category: String?,
        var timeInMillis: Long?,
        var webUrl: String?,
        var imgUrl: String?,
        var image: Bitmap?
)
