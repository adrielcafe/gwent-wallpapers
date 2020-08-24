package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import android.app.WallpaperManager
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperScreen
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperScreen.BOTH
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperScreen.HOME
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperScreen.LOCK

@RequiresApi(VERSION_CODES.N)
internal fun AutoWallpaperScreen.getFlags(): Int =
    when (this) {
        HOME -> WallpaperManager.FLAG_SYSTEM
        LOCK -> WallpaperManager.FLAG_LOCK
        BOTH -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
    }
