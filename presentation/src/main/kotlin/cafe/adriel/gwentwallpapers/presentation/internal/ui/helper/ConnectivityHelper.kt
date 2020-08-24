package cafe.adriel.gwentwallpapers.presentation.internal.ui.helper

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import androidx.viewbinding.ViewBinding
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.activity
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.coroutineScope
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.getNetworkCallback
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.openInternetSettings
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.showMessage
import cafe.adriel.gwentwallpapers.presentation.internal.model.MessageType
import kotlinx.coroutines.launch

internal class ConnectivityHelper(
    private val lifecycle: Lifecycle,
    private val manager: ConnectivityManager
) {

    private var isConnected = false

    private val networkCallback = getNetworkCallback { connected ->
        isConnected = connected
    }

    fun init() {
        lifecycle.addObserver(LifecycleEventObserver { _, event -> handleLifecycleEvent(event) })
    }

    fun runIfConnected(binding: ViewBinding, action: suspend () -> Unit) {
        when (isConnected) {
            true -> binding.coroutineScope.launch { action() }
            false -> binding.showMessage(
                type = MessageType.INFO,
                icon = R.drawable.ic_wifi_off,
                title = R.string.message_connect_to_internet,
                message = R.string.message_tap_to_open_settings,
                onClick = binding.activity::openInternetSettings
            )
        }
    }

    private fun handleLifecycleEvent(event: Event) {
        when (event) {
            Event.ON_START ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    manager.registerDefaultNetworkCallback(networkCallback)
                } else {
                    manager.registerNetworkCallback(
                        NetworkRequest.Builder()
                            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                            .build(),
                        networkCallback
                    )
                }
            Event.ON_STOP -> manager.unregisterNetworkCallback(networkCallback)
        }
    }
}
