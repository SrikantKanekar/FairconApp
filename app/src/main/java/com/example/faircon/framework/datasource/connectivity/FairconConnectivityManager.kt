package com.example.faircon.framework.datasource.connectivity

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FairconConnectivityManager
@Inject
constructor(
    private val wifiLiveData: WifiLiveData
) {

    // observe this in ui
    private val _available = MutableStateFlow<Boolean?>(null)
    val isAvailable: StateFlow<Boolean?> = _available

    fun registerWifiObserver(lifecycleOwner: LifecycleOwner) {
        wifiLiveData.observe(lifecycleOwner, { isConnected ->
            isConnected?.let { isAvailable ->
                _available.value = isAvailable
            }
        })
    }

    fun unregisterWifiObserver(lifecycleOwner: LifecycleOwner) {
        wifiLiveData.removeObservers(lifecycleOwner)
    }
}
