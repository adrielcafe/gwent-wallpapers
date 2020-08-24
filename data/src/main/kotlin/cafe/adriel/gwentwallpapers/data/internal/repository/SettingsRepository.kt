package cafe.adriel.gwentwallpapers.data.internal.repository

import cafe.adriel.gwentwallpapers.domain.interactor.GetSettingsInteractor
import cafe.adriel.gwentwallpapers.domain.interactor.UpdateSettingsInteractor
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaper
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperFrequency
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperScreen
import cafe.adriel.gwentwallpapers.domain.model.settings.Settings
import cafe.adriel.gwentwallpapers.domain.model.settings.Theme
import cafe.adriel.satchel.SatchelStorage
import cafe.adriel.satchel.ktx.value

internal class SettingsRepository(storage: SatchelStorage) {

    companion object {
        const val KEY_THEME = "settings.theme"
        const val KEY_AUTO_WALLPAPER_ENABLED = "settings.auto-wallpaper.enabled"
        const val KEY_AUTO_WALLPAPER_SHOW_NOTIFICATION = "settings.auto-wallpaper.show-notifications"
        const val KEY_AUTO_WALLPAPER_ONLY_FAVORITES = "settings.auto-wallpaper.only-favorites"
        const val KEY_AUTO_WALLPAPER_SCREEN = "settings.auto-wallpaper.screen"
        const val KEY_AUTO_WALLPAPER_FREQUENCY = "settings.auto-wallpaper.frequency"
    }

    val getSettings: GetSettingsInteractor = {
        Settings(
            theme = theme,
            autoWallpaper = AutoWallpaper(
                enabled = autoWallpaperEnabled,
                showNotification = autoWallpaperShowNotification,
                onlyFavorites = autoWallpaperOnlyFavorites,
                screen = autoWallpaperScreen,
                frequency = autoWallpaperFrequency
            )
        )
    }

    val updateSettings: UpdateSettingsInteractor = { settings ->
        theme = settings.theme
        autoWallpaperEnabled = settings.autoWallpaper.enabled
        autoWallpaperShowNotification = settings.autoWallpaper.showNotification
        autoWallpaperOnlyFavorites = settings.autoWallpaper.onlyFavorites
        autoWallpaperScreen = settings.autoWallpaper.screen
        autoWallpaperFrequency = settings.autoWallpaper.frequency
    }

    private var theme by storage.value(KEY_THEME, Theme.DEFAULT)
    private var autoWallpaperEnabled by storage.value(KEY_AUTO_WALLPAPER_ENABLED, false)
    private var autoWallpaperShowNotification by storage.value(KEY_AUTO_WALLPAPER_SHOW_NOTIFICATION, true)
    private var autoWallpaperOnlyFavorites by storage.value(KEY_AUTO_WALLPAPER_ONLY_FAVORITES, false)
    private var autoWallpaperScreen by storage.value(KEY_AUTO_WALLPAPER_SCREEN, AutoWallpaperScreen.BOTH)
    private var autoWallpaperFrequency by storage.value(KEY_AUTO_WALLPAPER_FREQUENCY, AutoWallpaperFrequency.EVERY_DAY)
}
