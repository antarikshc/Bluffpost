package com.antarikshc.theguardiannews.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.Uri
import android.os.Build

object Master {

    // Global params
    // Return API KEY
    const val apiKey = "753d66b9-55a1-4196-bc18-57c05d86c5ce"
    private const val BASE_SEARCH_URL = "https://content.guardianapis.com/search"
    private const val BASE_WORLD_URL = "https://content.guardianapis.com/world"

    // Return Uri.builder for Base API URL
    val searchUri: Uri.Builder
        get() {
            return Uri.parse(BASE_SEARCH_URL).buildUpon()
        }

    // Return Uri.builder for Base API URL
    val worldUri: Uri.Builder
        get() {
            return Uri.parse(BASE_WORLD_URL).buildUpon()
        }

    /**
     * Boilerplate to check network status
     *
     * @param context Activity context
     * @return Boolean indicating if Internet is connected
     */
    @SuppressWarnings("deprecation")
    fun checkNet(context: Context?): Boolean {
        val manager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT < 23) {
            manager.activeNetworkInfo?.run {
                isConnected && (type == TYPE_WIFI || type == TYPE_MOBILE)
            } ?: false
        } else {
            manager.activeNetwork?.run {
                val capabilities = manager.getNetworkCapabilities(this)
                capabilities.hasTransport(TRANSPORT_CELLULAR) || capabilities.hasTransport(TRANSPORT_WIFI)
            } ?: false
        }
    }
}
