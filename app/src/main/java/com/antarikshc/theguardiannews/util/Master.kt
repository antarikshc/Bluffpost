package com.antarikshc.theguardiannews.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri

object Master {

    // Global params
    // Return API KEY
    const val apiKey = "753d66b9-55a1-4196-bc18-57c05d86c5ce"
    private const val BASE_SEARCH_URL = "https://content.guardianapis.com/search"
    private const val BASE_WORLD_URL = "https://content.guardianapis.com/world"

    // Return Uri.builder for Base API URL
    val searchUri: Uri.Builder
        get() {
            val baseUri = Uri.parse(BASE_SEARCH_URL)
            return baseUri.buildUpon()
        }

    // Return Uri.builder for Base API URL
    val worldUri: Uri.Builder
        get() {
            val baseUri = Uri.parse(BASE_WORLD_URL)
            return baseUri.buildUpon()
        }

    /**
     * Boilerplate to check network status
     *
     * @param context Activity context
     * @return Boolean indicating if Internet is connected
     */
    fun checkNet(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo = cm.activeNetworkInfo
        return activeNetwork.isConnectedOrConnecting
    }
}
