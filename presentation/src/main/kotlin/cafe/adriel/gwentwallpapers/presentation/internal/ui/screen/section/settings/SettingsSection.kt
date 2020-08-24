package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings

import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperFrequency
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperScreen
import cafe.adriel.gwentwallpapers.domain.model.settings.Settings
import cafe.adriel.gwentwallpapers.domain.model.settings.Theme
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.SectionSettingsBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.activity
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.adapter
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.coroutineScope
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.koinScope
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.styleId
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.checkBox
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.section
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.selection
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.text
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings.SetAutoWallpaperEnabled
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings.SetAutoWallpaperFrequency
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings.SetAutoWallpaperOnlyFavorites
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings.SetAutoWallpaperScreen
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings.SetAutoWallpaperShowNotification
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsAction.UpdateSettings.SetTheme
import cafe.adriel.hal.currentState
import cafe.adriel.hal.emit
import cafe.adriel.hal.observeState
import com.mikepenz.fastadapter.dsl.itemAdapter
import daio.io.dresscode.dressCodeStyleId
import org.koin.androidx.viewmodel.scope.viewModel
import org.koin.core.KoinComponent

internal class SettingsSection(private val binding: SectionSettingsBinding) : KoinComponent {

    private val viewModel by binding.koinScope.viewModel<SettingsViewModel>(binding.activity)

    private val currentSettings: Settings
        get() = viewModel.currentState.settings

    private val settingsAdapter by adapter(binding.coroutineScope) {
        itemAdapter {
            section(
                titleRes = R.string.settings_theme_title,
                subtitleRes = R.string.settings_theme_subtitle
            )

            selection(
                values = Theme.values(),
                selectedValue = { currentSettings.theme },
                onSelected = { theme ->
                    viewModel emit SetTheme(theme)
                    theme.apply()
                }
            )

            section(
                titleRes = R.string.settings_auto_wallpaper_title,
                subtitleRes = R.string.settings_auto_wallpaper_subtitle,
                selected = { currentSettings.autoWallpaper.enabled },
                onChange = { selected -> viewModel emit SetAutoWallpaperEnabled(selected) }
            )

            checkBox(
                titleRes = R.string.settings_show_notification,
                selected = { currentSettings.autoWallpaper.showNotification },
                enabled = { currentSettings.autoWallpaper.enabled },
                onChange = { selected -> viewModel emit SetAutoWallpaperShowNotification(selected) }
            )

            checkBox(
                titleRes = R.string.settings_only_favorites,
                selected = { currentSettings.autoWallpaper.onlyFavorites },
                enabled = { currentSettings.autoWallpaper.enabled },
                onChange = { selected -> viewModel emit SetAutoWallpaperOnlyFavorites(selected) }
            )

            text(
                titleRes = R.string.settings_screen,
                enabled = { currentSettings.autoWallpaper.enabled }
            )

            selection(
                values = AutoWallpaperScreen.values(),
                selectedValue = { currentSettings.autoWallpaper.screen },
                enabled = { currentSettings.autoWallpaper.enabled },
                onSelected = { screen -> viewModel emit SetAutoWallpaperScreen(screen.type) }
            )

            text(
                titleRes = R.string.settings_frequency,
                enabled = { currentSettings.autoWallpaper.enabled }
            )

            selection(
                values = AutoWallpaperFrequency.values(),
                selectedValue = { currentSettings.autoWallpaper.frequency },
                enabled = { currentSettings.autoWallpaper.enabled },
                onSelected = { frequency -> viewModel emit SetAutoWallpaperFrequency(frequency.type) }
            )
        }
    }

    init {
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        binding.settingsList.apply {
            setItemViewCacheSize(settingsAdapter.itemCount)
            adapter = settingsAdapter
        }
    }

    private fun setupListeners() {
        viewModel.observeState(binding.coroutineScope) {
            settingsAdapter.notifyAdapterDataSetChanged()
        }
    }

    private fun Theme.apply() {
        binding.activity.dressCodeStyleId = styleId
    }
}
