package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import cafe.adriel.gwentwallpapers.domain.interactor.GetSettingsInteractor
import cafe.adriel.gwentwallpapers.domain.interactor.UpdateSettingsInteractor
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperFrequency
import cafe.adriel.gwentwallpapers.domain.model.settings.Settings
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.Init
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings.SetAutoWallpaperEnabled
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings.SetAutoWallpaperFrequency
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings.SetAutoWallpaperOnlyFavorites
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings.SetAutoWallpaperScreen
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings.SetAutoWallpaperShowNotification
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings.SetTheme
import cafe.adriel.gwentwallpapers.presentation.internal.worker.AutoWallpaperWorker
import cafe.adriel.hal.HAL
import cafe.adriel.hal.HAL.StateMachine
import cafe.adriel.hal.emit
import kotlinx.coroutines.CoroutineDispatcher

internal class SettingsViewModel(
    private val getSettings: GetSettingsInteractor,
    private val updateSettings: UpdateSettingsInteractor,
    private val workManager: WorkManager,
    dispatcher: CoroutineDispatcher
) : ViewModel(), StateMachine<SettingsAction, SettingsState> {

    override val stateMachine by HAL(SettingsState(), viewModelScope, dispatcher) { action, state ->
        when (action) {
            is Init -> +state.copy(settings = getSettings())

            is UpdateSettings ->
                state.settings
                    .mutateWith(action)
                    .also { updateSettings(it) }
                    .let { +state.copy(settings = it) }
        }
    }

    init {
        emit(Init)
    }

    private fun Settings.mutateWith(action: UpdateSettings): Settings =
        when (action) {
            is SetTheme -> copy(theme = action.value)

            is SetAutoWallpaperEnabled ->
                copy(autoWallpaper = autoWallpaper.copy(enabled = action.value))
                    .apply { updateAutoWallpaperWork(autoWallpaper.enabled, autoWallpaper.frequency, delayed = false) }

            is SetAutoWallpaperShowNotification ->
                copy(autoWallpaper = autoWallpaper.copy(showNotification = action.value))

            is SetAutoWallpaperOnlyFavorites ->
                copy(autoWallpaper = autoWallpaper.copy(onlyFavorites = action.value))

            is SetAutoWallpaperScreen ->
                copy(autoWallpaper = autoWallpaper.copy(screen = action.value))

            is SetAutoWallpaperFrequency ->
                copy(autoWallpaper = autoWallpaper.copy(frequency = action.value))
                    .apply { updateAutoWallpaperWork(autoWallpaper.enabled, autoWallpaper.frequency, delayed = true) }
        }

    private fun updateAutoWallpaperWork(enabled: Boolean, frequency: AutoWallpaperFrequency, delayed: Boolean) {
        when (enabled) {
            true -> AutoWallpaperWorker.enqueueWork(workManager, frequency, delayed)
            false -> AutoWallpaperWorker.cancelWork(workManager)
        }
    }
}
