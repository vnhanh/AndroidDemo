package com.vnhanh.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission

interface INetworkChecker {
    fun isInternetAvailable() : Boolean
}

class InternetChecker (private val context: Context) : INetworkChecker {
    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    override fun isInternetAvailable(): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return false
        val activeNetwork: Network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities: NetworkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
