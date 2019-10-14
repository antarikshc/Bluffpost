package com.antarikshc.theguardiannews.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.antarikshc.theguardiannews.model.NewsData
import com.antarikshc.theguardiannews.ui.WebviewActivity

fun Context.openBrowser(news: NewsData?) {
    news?.let { it ->
        try {
            //Explicit Intent
            val webActivityIntent = Intent(this, WebviewActivity::class.java)
            webActivityIntent.putExtra("url", it.webUrl)
            startActivity(webActivityIntent)
        } catch (e: Exception) {
            //Implicit Intent
            val webUri = Uri.parse(news.webUrl)
            val webBrowserIntent = Intent(Intent.ACTION_VIEW, webUri)
            webBrowserIntent.resolveActivity(packageManager)?.run {
                startActivity(webBrowserIntent)
            }
        }
    }
}
