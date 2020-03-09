package com.apdallahy3.accenturetask.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkConnectionUtils {
    companion object {
        fun checkInternet(context: Context): Boolean {
            var isConnected: Boolean? = null
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetwork == null) {

                isConnected = false
            } else {
                isConnected = activeNetwork?.isConnected!!

            }
            return isConnected!!
        }
    }
}