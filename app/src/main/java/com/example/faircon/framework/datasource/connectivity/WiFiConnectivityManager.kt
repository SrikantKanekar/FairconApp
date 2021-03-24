package com.example.faircon.framework.datasource.connectivity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WiFiConnectivityManager
@Inject
constructor(
    private val wiFiLiveData: WiFiLiveData
) {

    // observe this in ui
    var isWiFiAvailable by mutableStateOf(true)

    fun registerWiFiObserver(lifecycleOwner: LifecycleOwner) {
        wiFiLiveData.observe(lifecycleOwner, { isConnected ->
            isConnected?.let { isAvailable ->
                isWiFiAvailable = isAvailable
            }
        })
    }

    fun unregisterWiFiObserver(lifecycleOwner: LifecycleOwner) {
        wiFiLiveData.removeObservers(lifecycleOwner)
    }
}
