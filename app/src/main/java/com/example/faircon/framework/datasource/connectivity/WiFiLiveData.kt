package com.example.faircon.framework.datasource.connectivity

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import com.example.faircon.business.domain.util.printLogNetwork
import com.example.faircon.business.interactors.app.IsConnectedToFaircon
import com.example.faircon.framework.presentation.ui.BaseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Checks if FAIRCON is connected.
 */
@Singleton
class WiFiLiveData
@Inject
constructor(
    app: BaseApplication,
    private val isConnectedToFaircon: IsConnectedToFaircon
) : LiveData<Boolean>() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val connectivityManager =
        app.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private var isConnected = false

    override fun onActive() {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            printLogNetwork("WiFiLiveData", "onAvailable: Network ID : $network")
            CoroutineScope(Dispatchers.IO).launch {
                isConnected = isConnectedToFaircon.execute()
                printLogNetwork(
                    "WiFiLiveData",
                    "onAvailable: Is Connected to FAIRCON : $isConnected"
                )
                postValue(isConnected)
            }
        }

        override fun onLost(network: Network) {
            printLogNetwork("WiFiLiveData", "onLost: Network ID : $network")
            CoroutineScope(Dispatchers.IO).launch {
                isConnected = isConnectedToFaircon.execute()
                printLogNetwork(
                    "WiFiLiveData",
                    "onLost: Is Connected to FAIRCON : $isConnected"
                )
                postValue(isConnected)
            }
        }
    }
}