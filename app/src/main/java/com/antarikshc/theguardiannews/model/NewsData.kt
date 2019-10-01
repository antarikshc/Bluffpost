package com.antarikshc.theguardiannews.model

import android.graphics.Bitmap

data class NewsData(var title: String? = null,
                    var author: String? = null,
                    var category: String? = null,
                    var timeInMillis: Long? = null,
                    var webUrl: String? = null,
                    var imgUrl: String? = null,
                    var image: Bitmap? = null) {
}