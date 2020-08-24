package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater

internal val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)

internal val Context.statusBarHeight: Int
    get() = resources
        .getIdentifier("status_bar_height", "dimen", "android")
        .takeIf { it > 0 }
        ?.let(resources::getDimensionPixelSize)
        ?: 0

internal fun Context.openAppSettings() {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .setData(Uri.fromParts("package", packageName, null))
        .putExtra("android.provider.extra.APP_PACKAGE", packageName)
        .putExtra("app_package", packageName)
        .putExtra("app_uid", applicationInfo.uid)
        .let(::startActivity)
}

internal fun Context.openInternetSettings() {
    val action = when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        true -> Settings.Panel.ACTION_INTERNET_CONNECTIVITY
        false -> Settings.ACTION_WIRELESS_SETTINGS
    }
    Intent(action)
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .let(::startActivity)
}
