package com.example.faircon.network.fairconConnection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.faircon.presentation.ui.BaseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * * Monitors the connection of the app to the faircon server.
 * * It pings the faircon server everytime there are changes in wifi connections.
 */
@Singleton
class FairconConnection
@Inject
constructor(
    application: BaseApplication,
    private val pingFaircon: PingFaircon
) {

    private val _available = MutableStateFlow<Boolean?>(null)
    val available: StateFlow<Boolean?> = _available

    private var networkCallback: ConnectivityManager.NetworkCallback
    private val connectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            CoroutineScope(Dispatchers.IO).launch {
                _available.value = pingFaircon.execute()
            }
        }

        override fun onLost(network: Network) {
            CoroutineScope(Dispatchers.IO).launch {
                _available.value = pingFaircon.execute()
            }
        }
    }
}
