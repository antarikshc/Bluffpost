package com.antarikshc.theguardiannews.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.antarikshc.theguardiannews.model.NewsData
import org.json.JSONArray
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
import java.util.ArrayList

object ConnectAPI {

    val LOG_TAG = ConnectAPI::class.java.simpleName
    /**
     * to be called from other activities, returns NewsData object
     */
    fun fetchNewsData(urls: String): ArrayList<NewsData>? {

        if (urls.isEmpty() || urls == null) {
            return null
        }

        // Create URL object
        val url = createUrl(urls)

        // Perform HTTP request to the URL and receive a JSON response back
        var jsonResponse = ""
        try {
            jsonResponse = makeHttpRequest(url)
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e)
        }

        return extractNews(jsonResponse)
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private fun createUrl(stringUrl: String): URL? {
        val url: URL
        try {
            url = URL(stringUrl)
        } catch (exception: MalformedURLException) {
            Log.e(LOG_TAG, "Error while creating URL", exception)
            return null
        }

        return url
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    @Throws(IOException::class)
    private fun makeHttpRequest(url: URL?): String {
        var jsonResponse = ""

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse
        }

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
        return jsonResponse
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

            val results: JSONArray
            if (response.has("editorsPicks") && !response.isNull("editorsPicks")) {
                results = response.getJSONArray("editorsPicks")
            } else {
                results = response.getJSONArray("results")
            }

            for (i in 0 until results.length()) {

                //Initialize everything with null to perform null checks
                var selection: String? = null
                var title: String? = null
                var author: String? = null
                var dateTimeInString: String? = null
                var timeInMills: Long? = null
                var fields: JSONObject? = null
                var imgUrl: String? = null

                val newsItem = results.getJSONObject(i)

                selection = newsItem.getString("sectionName")

                if (newsItem.has("webPublicationDate") && !newsItem.isNull("webPublicationDate")) {
                    //webPublicationDate contains both date and time, we format it into millis to get relative time
                    dateTimeInString = newsItem.getString("webPublicationDate")
                    try {

                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        timeInMills = sdf.parse(dateTimeInString).time

                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                }

                val url = newsItem.getString("webUrl")

                //check if fields is not null, else re run the for loop
                //items without thumbnails arent news articles.
                if (newsItem.isNull("fields")) {
                    continue
                } else {
                    fields = newsItem.getJSONObject("fields")
                }

                if (fields!!.isNull("thumbnail")) {
                    continue
                } else {
                    imgUrl = fields.getString("thumbnail")
                }


                if (fields.has("headline") && !fields.isNull("headline")) {
                    title = fields.getString("headline")
                } else {
                    title = newsItem.getString("webTitle")
                }

                if (fields.has("byline") && !fields.isNull("byline")) {
                    author = fields.getString("byline")
                }

                //downloading thumbnails
                var coverImage: Bitmap? = null
                // Create URL object
                if (imgUrl != null) {
                    val imageUrl = createUrl(imgUrl)
                    try {
                        val connection = imageUrl!!.openConnection() as HttpURLConnection
                        connection.connect()
                        val inputStream = connection.inputStream
                        coverImage = BitmapFactory.decodeStream(inputStream)
                    } catch (e: Exception) {
                        Log.e(LOG_TAG, "Problem encountered getting image from HTTP url.", e)
                    }

                }

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

}