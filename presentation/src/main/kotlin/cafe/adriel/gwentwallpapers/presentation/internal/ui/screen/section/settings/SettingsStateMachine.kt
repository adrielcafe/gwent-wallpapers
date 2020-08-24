package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings

import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperFrequency
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperScreen
import cafe.adriel.gwentwallpapers.domain.model.settings.Settings
import cafe.adriel.gwentwallpapers.domain.model.settings.Theme
import cafe.adriel.hal.HAL

internal sealed class SettingsAction : HAL.Action {
    object Init : SettingsAction()

    sealed class UpdateSettings : SettingsAction() {
        data class SetTheme(val value: Theme) : UpdateSettings()
        data class SetAutoWallpaperEnabled(val value: Boolean) : UpdateSettings()
        data class SetAutoWallpaperShowNotification(val value: Boolean) : UpdateSettings()
        data class SetAutoWallpaperOnlyFavorites(val value: Boolean) : UpdateSettings()
        data class SetAutoWallpaperScreen(val value: AutoWallpaperScreen) : UpdateSettings()
        data class SetAutoWallpaperFrequency(val value: AutoWallpaperFrequency) : UpdateSettings()
    }
}

internal data class SettingsState(
    val settings: Settings = Settings()
) : HAL.State
