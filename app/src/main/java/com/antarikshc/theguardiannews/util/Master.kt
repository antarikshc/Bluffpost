package com.antarikshc.theguardiannews.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri

class Master {

    companion object {
        private val API_KEY = "753d66b9-55a1-4196-bc18-57c05d86c5ce"
        private val BASE_SEARCH_URL = "https://content.guardianapis.com/search"
        private val BASE_WORLD_URL = "https://content.guardianapis.com/world"

        // Return API KEY
        fun getAPIKey(): String {
            return API_KEY
        }

        // Return Uri.builder for Base API URL
        fun getSearchUri(): Uri.Builder {
            val baseUri = Uri.parse(BASE_SEARCH_URL)
            return baseUri.buildUpon()
        }

        // Return Uri.builder for Base API URL
        fun getWorldUri(): Uri.Builder {
            val baseUri = Uri.parse(BASE_WORLD_URL)
            return baseUri.buildUpon()
        }

        /**
         * Boilerplate to check network status
         *
         * @param context Activity context
         * @return Boolean indicating if Internet is connected
         */
        fun checkNet(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork: NetworkInfo? = null
            if (cm != null) {
                activeNetwork = cm.activeNetworkInfo
            }
            return activeNetwork != null && activeNetwork.isConnected
        }
    }


}