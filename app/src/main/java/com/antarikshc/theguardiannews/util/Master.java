package com.antarikshc.theguardiannews.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class Master {

    // Global params
    private static final String API_KEY = "753d66b9-55a1-4196-bc18-57c05d86c5ce";
    private static final String BASE_SEARCH_URL = "https://content.guardianapis.com/search";
    private static final String BASE_WORLD_URL = "https://content.guardianapis.com/world";

    // Return API KEY
    public static String getAPIKey() {
        return API_KEY;
    }

    // Return Uri.builder for Base API URL
    public static Uri.Builder getSearchUri() {
        Uri baseUri = Uri.parse(BASE_SEARCH_URL);
        return baseUri.buildUpon();
    }

    // Return Uri.builder for Base API URL
    public static Uri.Builder getWorldUri() {
        Uri baseUri = Uri.parse(BASE_WORLD_URL);
        return baseUri.buildUpon();
    }

    /**
     * Boilerplate to check network status
     *
     * @param context Activity context
     * @return Boolean indicating if Internet is connected
     */
    public static boolean checkNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
