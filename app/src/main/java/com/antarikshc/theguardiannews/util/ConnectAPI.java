package com.antarikshc.theguardiannews.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.antarikshc.theguardiannews.model.NewsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ConnectAPI {

    private static final String LOG_TAG = ConnectAPI.class.getSimpleName();

    /**
     * Constructor to prevent making object of class
     **/
    private ConnectAPI() {
    }

    /**
     * to be called from other activities, returns NewsData object
     **/
    public static ArrayList<NewsData> fetchNewsData(String urls) {

        if (urls.isEmpty() || urls == null) {
            return null;
        }

        // Create URL object
        URL url = createUrl(urls);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        ArrayList<NewsData> fetchedData = extractNews(jsonResponse);

        return fetchedData;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error while creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Get JSON response from server as InputSteam and store as String
     **/
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Parse JSON response and extract items to be stored in BookData
     **/
    private static ArrayList<NewsData> extractNews(String jsonResponse) {

        // Create an empty ArrayList that we can start adding books to
        ArrayList<NewsData> news = new ArrayList<>();

        // Try to parse the jsonResponse. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject root = new JSONObject(jsonResponse);

            JSONObject response = root.getJSONObject("response");

            JSONArray results;
            if (response.has("editorsPicks") && !response.isNull("editorsPicks")) {
                results = response.getJSONArray("editorsPicks");
            } else {
                results = response.getJSONArray("results");
            }

            for (int i = 0; i < results.length(); i++) {

                //Initialize everything with null to perform null checks
                String selection = null;
                String title = null;
                String author = null;
                String dateTimeInString = null;
                Long timeInMills = null;
                JSONObject fields = null;
                String imgUrl = null;

                JSONObject newsItem = results.getJSONObject(i);

                selection = newsItem.getString("sectionName");

                if (newsItem.has("webPublicationDate") && !newsItem.isNull("webPublicationDate")) {
                    //webPublicationDate contains both date and time, we format it into millis to get relative time
                    dateTimeInString = newsItem.getString("webPublicationDate");
                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        timeInMills = sdf.parse(dateTimeInString).getTime();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                String url = newsItem.getString("webUrl");

                //check if fields is not null, else re run the for loop
                //items without thumbnails arent news articles.
                if (newsItem.isNull("fields")) {
                    continue;
                } else {
                    fields = newsItem.getJSONObject("fields");
                }

                if (fields.isNull("thumbnail")) {
                    continue;
                } else {
                    imgUrl = fields.getString("thumbnail");
                }


                if (fields.has("headline") && !fields.isNull("headline")) {
                    title = fields.getString("headline");
                } else {
                    title = newsItem.getString("webTitle");
                }

                if (newsItem.has("tags") && !newsItem.isNull("tags")) {
                    JSONArray tags = newsItem.getJSONArray("tags");
                    JSONObject tagsObject = tags.getJSONObject(0);
                    author = tagsObject.getString("webTitle");
                } else if (fields.has("byline") && !fields.isNull("byline")) {
                    author = fields.getString("byline");
                }

                //downloading thumbnails
                Bitmap coverImage = null;
                // Create URL object
                if (imgUrl != null) {
                    URL imageUrl = createUrl(imgUrl);
                    try {
                        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                        connection.connect();
                        InputStream inputStream = connection.getInputStream();
                        coverImage = BitmapFactory.decodeStream(inputStream);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Problem encountered getting image from HTTP url.", e);
                    }
                }

                news.add(new NewsData(title, author, selection, timeInMills, url, imgUrl, coverImage));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return news;
    }


}
