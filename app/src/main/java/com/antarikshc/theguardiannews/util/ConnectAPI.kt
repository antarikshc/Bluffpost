package com.antarikshc.theguardiannews.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.antarikshc.theguardiannews.model.NewsData
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object ConnectAPI {

    private val LOG_TAG = ConnectAPI::class.java.simpleName

    /**
     * to be called from other activities, returns NewsData object
     */
    fun fetchNewsData(urls: String): ArrayList<NewsData>? {
        return if (urls.isNotBlank()) {
            // Create URL object
            val url = createUrl(urls)
            // Perform HTTP request to the URL and receive a JSON response back
            val jsonResponse = makeHttpRequest(url)
            try {
                extractNews(jsonResponse)
            } catch (e: IOException) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e)
                null
            }
        } else {
            null
        }
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private fun createUrl(stringUrl: String): URL? {
        return try {
            URL(stringUrl)
        } catch (exception: MalformedURLException) {
            Log.e(LOG_TAG, "Error while creating URL", exception)
            null
        }
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private fun makeHttpRequest(url: URL?): String {
        // If the URL is null, then return early.
        return url?.run {
            var jsonResponse = ""
            var urlConnection: HttpURLConnection? = null
            var inputStream: InputStream? = null

            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 10000
                urlConnection.connectTimeout = 15000
                urlConnection.connect()
                if (urlConnection.responseCode == 200) {
                    inputStream = urlConnection.inputStream
                    jsonResponse = readFromStream(inputStream)
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.responseCode)
                }
            } catch (e: IOException) {
                Log.e(LOG_TAG, "Problem retrieving the books JSON results.", e)
            } finally {
                urlConnection?.disconnect()
                inputStream?.close()
            }
            jsonResponse
        } ?: ""
    }

    /**
     * Get JSON response from server as InputSteam and store as String
     */
    @Throws(IOException::class)
    private fun readFromStream(inputStream: InputStream?): String {
        val output = StringBuilder()
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
            val reader = BufferedReader(inputStreamReader)
            var line: String? = reader.readLine()
            while (line != null) {
                output.append(line)
                line = reader.readLine()
            }
        }
        return output.toString()
    }

    /**
     * Parse JSON response and extract items to be stored in BookData
     */
    private fun extractNews(jsonResponse: String): ArrayList<NewsData> {
        // Create an empty ArrayList that we can start adding books to
        val news = ArrayList<NewsData>()

        // Try to parse the jsonResponse. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            Log.i("JSON ", "Response: $jsonResponse")
            val root = JSONObject(jsonResponse)
            val response = root.getJSONObject("response")
            val results = if (response.has("editorsPicks") && !response.isNull("editorsPicks")) {
                response.getJSONArray("editorsPicks")
            } else {
                response.getJSONArray("results")
            }

            for (i in 0 until results.length()) {
                // Initialize everything with null to perform null checks
                val newsItem = results.getJSONObject(i)
                val selection: String? = newsItem.getString("sectionName")
                // webPublicationDate contains both date and time, we format it into millis to get relative time
                val dateTimeInString = newsItem.getSafe("webPublicationDate")
                val timeInMills: Long? = parseDate(dateTimeInString)
                val url = newsItem.getString("webUrl")

                // check if fields is not null, else re run the for loop
                // items without thumbnails arent news articles.
                val fields: JSONObject = if (!newsItem.isNull("fields")) {
                    newsItem.getJSONObject("fields")
                } else {
                    JSONObject()
                }
                var title = fields.getSafe("headline")
                if (title == null) {
                    title = newsItem.getString("webTitle")
                }
                val author = fields.getSafe("byline")
                val imgUrl: String? = fields.getSafe("thumbnail")
                // downloading thumbnails
                val coverImage: Bitmap? = downloadBitmap(imgUrl)

                news.add(NewsData(title, author, selection, timeInMills, url, imgUrl, coverImage))
            }
        } catch (e: JSONException) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e)
        }
        // Return the list of news
        return news
    }

    @SuppressLint("SimpleDateFormat")
    private fun parseDate(dateTime: String?): Long? {
        return try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateTime).time
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    private fun downloadBitmap(imgUrl: String?): Bitmap? {
        return imgUrl?.run {
            try {
                // Create URL object
                val connection = createUrl(this)?.openConnection() as HttpURLConnection
                connection.connect()
                BitmapFactory.decodeStream(connection.inputStream)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Problem encountered getting image from HTTP url.", e)
                null
            }
        }
    }
}

fun JSONObject.getSafe(key: String): String? {
    return if (!this.isNull(key) && this.has(key)) {
        this.getString(key)
    } else null
}