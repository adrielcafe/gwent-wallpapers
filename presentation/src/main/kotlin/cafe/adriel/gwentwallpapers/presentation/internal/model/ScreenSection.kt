package cafe.adriel.gwentwallpapers.presentation.internal.model

import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import cafe.adriel.gwentwallpapers.presentation.R

internal enum class ScreenSection(
    @IdRes val itemId: Int,
    @MenuRes val menuId: Int,
    val whiteStatusBar: Boolean
) {
    WALLPAPERS(R.id.action_wallpapers, R.menu.section_wallpapers, true),
    SETTINGS(R.id.action_settings, R.menu.section_settings, false),
    ABOUT(R.id.action_about, R.menu.section_about, false)
}
