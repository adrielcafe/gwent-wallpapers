package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings

import androidx.work.WorkManager
import cafe.adriel.gwentwallpapers.domain.interactor.GetSettingsInteractor
import cafe.adriel.gwentwallpapers.domain.interactor.UpdateSettingsInteractor
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaper
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperFrequency
import cafe.adriel.gwentwallpapers.domain.model.settings.Settings
import cafe.adriel.gwentwallpapers.domain.model.settings.Theme
import cafe.adriel.hal.HAL.StateMachine
import cafe.adriel.hal.currentState
import cafe.adriel.hal.plusAssign
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class SettingsStateMachineTest {

    @BeforeEach
    fun init() {
        `getSettings returns settings`()
        `updateSettings completes`()
        `workManager enqueues work`()
        `workManager cancels work`()
    }

    @Nested
    inner class Init {

        @Test
        fun `should get settings`() {
            stateMachine += SettingsAction.Init

            coVerify { getSettings() }

            expectThat(stateMachine.currentState) isEqualTo SettingsState(Settings())
        }
    }

    @Nested
    inner class SetTheme {

        @Test
        fun `should update settings`() {
            val settings = Settings(theme = Theme.SCOIATAEL)

            stateMachine += SettingsAction.UpdateSettings.SetTheme(Theme.SCOIATAEL)

            coVerify { updateSettings(settings) }

            expectThat(stateMachine.currentState) isEqualTo SettingsState(settings)
        }
    }

    @Nested
    inner class SetAutoWallpaperEnabled {

        @Test
        fun `when is enabled then enqueue work and update settings`() {
            val settings = Settings(autoWallpaper = AutoWallpaper().copy(enabled = true))

            stateMachine += SettingsAction.UpdateSettings.SetAutoWallpaperEnabled(true)

            coVerifySequence {
                workManager.enqueueUniquePeriodicWork(any(), any(), any())
                updateSettings(settings)
            }

            expectThat(stateMachine.currentState) isEqualTo SettingsState(settings)
        }

        @Test
        fun `when is disabled then cancel work and update settings`() {
            val settings = Settings(autoWallpaper = AutoWallpaper().copy(enabled = false))

            stateMachine += SettingsAction.UpdateSettings.SetAutoWallpaperEnabled(false)

            coVerifySequence {
                workManager.cancelUniqueWork(any())
                updateSettings(settings)
            }

            expectThat(stateMachine.currentState) isEqualTo SettingsState(settings)
        }
    }

    @Nested
    inner class SetAutoWallpaperFrequency {

        @Test
        fun `should update settings`() {
            val settings = Settings(
                autoWallpaper = AutoWallpaper().copy(frequency = AutoWallpaperFrequency.EVERY_MONTH)
            )

            stateMachine += SettingsAction.UpdateSettings.SetAutoWallpaperFrequency(AutoWallpaperFrequency.EVERY_MONTH)

            coVerifySequence { updateSettings(settings) }

            expectThat(stateMachine.currentState) isEqualTo SettingsState(settings)
        }
    }

    @Nested
    inner class SetAutoWallpaperShowNotification {

        @Test
        fun `should update settings`() {
            val settings = Settings(autoWallpaper = AutoWallpaper().copy(showNotification = true))

            stateMachine += SettingsAction.UpdateSettings.SetAutoWallpaperShowNotification(true)

            coVerifySequence { updateSettings(settings) }

            expectThat(stateMachine.currentState) isEqualTo SettingsState(settings)
        }
    }

    @Nested
    inner class SetAutoWallpaperOnlyFavorites {

        @Test
        fun `should update settings`() {
            val settings = Settings(autoWallpaper = AutoWallpaper().copy(onlyFavorites = true))

            stateMachine += SettingsAction.UpdateSettings.SetAutoWallpaperOnlyFavorites(true)

            coVerifySequence { updateSettings(settings) }

            expectThat(stateMachine.currentState) isEqualTo SettingsState(settings)
        }
    }

    private fun `getSettings returns settings`() {
        coEvery { getSettings() } returns Settings()
    }

    private fun `updateSettings completes`() {
        coEvery { updateSettings(any()) } returns Unit
    }

    private fun `workManager enqueues work`() {
        coEvery { workManager.enqueueUniquePeriodicWork(any(), any(), any()) } returns mockk()
    }

    private fun `workManager cancels work`() {
        coEvery { workManager.cancelUniqueWork(any()) } returns mockk()
    }

    private val getSettings = mockk<GetSettingsInteractor>()
    private val updateSettings = mockk<UpdateSettingsInteractor>()
    private val workManager = mockk<WorkManager>()

    private val stateMachine: StateMachine<SettingsAction, SettingsState> by lazy {
        spyk(SettingsViewModel(getSettings, updateSettings, workManager, TestCoroutineDispatcher()))
    }
}
