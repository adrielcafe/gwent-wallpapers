package cafe.adriel.gwentwallpapers.data.internal.repository

import cafe.adriel.gwentwallpapers.data.internal.repository.SettingsRepository.Companion.KEY_AUTO_WALLPAPER_ENABLED
import cafe.adriel.gwentwallpapers.data.internal.repository.SettingsRepository.Companion.KEY_AUTO_WALLPAPER_FREQUENCY
import cafe.adriel.gwentwallpapers.data.internal.repository.SettingsRepository.Companion.KEY_AUTO_WALLPAPER_ONLY_FAVORITES
import cafe.adriel.gwentwallpapers.data.internal.repository.SettingsRepository.Companion.KEY_AUTO_WALLPAPER_SCREEN
import cafe.adriel.gwentwallpapers.data.internal.repository.SettingsRepository.Companion.KEY_AUTO_WALLPAPER_SHOW_NOTIFICATION
import cafe.adriel.gwentwallpapers.data.internal.repository.SettingsRepository.Companion.KEY_THEME
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaper
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperFrequency
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperScreen
import cafe.adriel.gwentwallpapers.domain.model.settings.Settings
import cafe.adriel.gwentwallpapers.domain.model.settings.Theme
import cafe.adriel.satchel.SatchelStorage
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class SettingsRepositoryTest {

    @Nested
    inner class GetSettings {

        @Test
        fun `when storage has settings then return stored settings`() = runBlockingTest {
            `storage has settings`()

            val result = repository.getSettings()

            expectThat(result) isEqualTo storedSettings
        }

        @Test
        fun `when storage has no settings then return default settings`() = runBlockingTest {
            `storage is empty`()

            val result = repository.getSettings()

            expectThat(result) isEqualTo defaultSettings
        }
    }

    @Nested
    inner class UpdateSettings {

        @Test
        fun `when update settings then store settings`() = runBlockingTest {
            `storage set value`()

            repository.updateSettings(storedSettings)

            coVerify {
                storage[KEY_THEME] = storedSettings.theme
                storage[KEY_AUTO_WALLPAPER_ENABLED] = storedSettings.autoWallpaper.enabled
                storage[KEY_AUTO_WALLPAPER_SHOW_NOTIFICATION] = storedSettings.autoWallpaper.showNotification
                storage[KEY_AUTO_WALLPAPER_ONLY_FAVORITES] = storedSettings.autoWallpaper.onlyFavorites
                storage[KEY_AUTO_WALLPAPER_SCREEN] = storedSettings.autoWallpaper.screen
                storage[KEY_AUTO_WALLPAPER_FREQUENCY] = storedSettings.autoWallpaper.frequency
            }
        }
    }

    private fun `storage has settings`() {
        every {
            storage.getAny(KEY_THEME)
        } returns storedSettings.theme

        every {
            storage.getAny(KEY_AUTO_WALLPAPER_ENABLED)
        } returns storedSettings.autoWallpaper.enabled

        every {
            storage.getAny(KEY_AUTO_WALLPAPER_SHOW_NOTIFICATION)
        } returns storedSettings.autoWallpaper.showNotification

        every {
            storage.getAny(KEY_AUTO_WALLPAPER_ONLY_FAVORITES)
        } returns storedSettings.autoWallpaper.onlyFavorites

        every {
            storage.getAny(KEY_AUTO_WALLPAPER_SCREEN)
        } returns storedSettings.autoWallpaper.screen

        every {
            storage.getAny(KEY_AUTO_WALLPAPER_FREQUENCY)
        } returns storedSettings.autoWallpaper.frequency
    }

    private fun `storage is empty`() {
        every {
            storage.getAny(KEY_THEME)
        } returns defaultSettings.theme

        every {
            storage.getAny(KEY_AUTO_WALLPAPER_ENABLED)
        } returns defaultSettings.autoWallpaper.enabled

        every {
            storage.getAny(KEY_AUTO_WALLPAPER_SHOW_NOTIFICATION)
        } returns defaultSettings.autoWallpaper.showNotification

        every {
            storage.getAny(KEY_AUTO_WALLPAPER_ONLY_FAVORITES)
        } returns defaultSettings.autoWallpaper.onlyFavorites

        every {
            storage.getAny(KEY_AUTO_WALLPAPER_SCREEN)
        } returns defaultSettings.autoWallpaper.screen

        every {
            storage.getAny(KEY_AUTO_WALLPAPER_FREQUENCY)
        } returns defaultSettings.autoWallpaper.frequency
    }

    private fun `storage set value`() {
        every { storage.set<String>(any(), any()) } returns Unit
        every { storage.set<Boolean>(any(), any()) } returns Unit
        every { storage.set<Long>(any(), any()) } returns Unit
    }

    private val storage = mockk<SatchelStorage>()

    private val repository = SettingsRepository(storage)

    private val storedSettings = Settings(
        theme = Theme.NORTHERN_REALMS,
        autoWallpaper = AutoWallpaper(
            enabled = true,
            showNotification = false,
            onlyFavorites = true,
            screen = AutoWallpaperScreen.LOCK,
            frequency = AutoWallpaperFrequency.EVERY_HOUR
        )
    )

    private val defaultSettings = Settings()
}
