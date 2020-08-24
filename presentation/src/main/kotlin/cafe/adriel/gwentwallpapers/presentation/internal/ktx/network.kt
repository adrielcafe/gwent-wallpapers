package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import android.net.ConnectivityManager
import android.net.Network

internal fun getNetworkCallback(onChange: (connected: Boolean) -> Unit): ConnectivityManager.NetworkCallback =
    object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) = onChange(true)

        override fun onLost(network: Network) = onChange(false)
    }
