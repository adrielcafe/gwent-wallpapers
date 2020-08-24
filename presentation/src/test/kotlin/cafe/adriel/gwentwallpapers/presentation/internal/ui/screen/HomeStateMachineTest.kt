package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen

import cafe.adriel.broker.BrokerPublisher
import cafe.adriel.gwentwallpapers.domain.interactor.ToggleFavoriteInteractor
import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide.FRONT
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter
import cafe.adriel.gwentwallpapers.presentation.internal.model.ScreenSection
import cafe.adriel.gwentwallpapers.presentation.internal.model.WallpaperEvent
import cafe.adriel.gwentwallpapers.presentation.internal.model.WallpaperEvent.FavoriteChanged
import cafe.adriel.hal.HAL.StateMachine
import cafe.adriel.hal.currentState
import cafe.adriel.hal.plusAssign
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class HomeStateMachineTest {

    @BeforeEach
    fun init() {
        `toggleFavorite returns new status`()
        `eventPublisher publishes event`()
        `eventPublisher publishes retained event`()
    }

    @Nested
    inner class Init {

        @Test
        fun `should publish event`() {
            stateMachine += HomeAction.Init

            coVerify {
                eventPublisher.publish(
                    event = WallpaperEvent.SearchRequested(SearchFilter(), SearchSorter.Random),
                    retain = true
                )
            }

            expectThat(stateMachine.currentState) isEqualTo HomeState()
        }
    }

    @Nested
    inner class Navigate {

        @Test
        fun `should update state`() {
            stateMachine += HomeAction.Navigate(ScreenSection.SETTINGS)

            expectThat(stateMachine.currentState) isEqualTo HomeState(section = ScreenSection.SETTINGS)
        }
    }

    @Nested
    inner class UpdateSearchFilter {

        @Test
        fun `should update state and publish event`() {
            val filter = SearchFilter(side = CardSide.BACK)

            stateMachine += HomeAction.UpdateSearchFilter(filter)

            coVerify { eventPublisher.publish(WallpaperEvent.SearchRequested(filter, SearchSorter.Random)) }

            expectThat(stateMachine.currentState) isEqualTo HomeState(filter = filter)
        }
    }

    @Nested
    inner class UpdateSearchSorter {

        @Test
        fun `should update state and publish event`() {
            val sorter = SearchSorter.Name(ascending = true)

            stateMachine += HomeAction.UpdateSearchSorter(sorter)

            coVerify { eventPublisher.publish(WallpaperEvent.SearchRequested(SearchFilter(), sorter)) }

            expectThat(stateMachine.currentState) isEqualTo HomeState(sorter = sorter)
        }
    }

    @Nested
    inner class ToggleFavorite {

        @Test
        fun `should toggle favorite and publish event`() {
            stateMachine += HomeAction.ToggleFavorite(sampleWallpaper)

            coVerifyOrder {
                toggleFavorite(sampleWallpaper)
                eventPublisher.publish(FavoriteChanged(sampleWallpaper, true))
            }

            expectThat(stateMachine.currentState) isEqualTo HomeState()
        }
    }

    private fun `toggleFavorite returns new status`() {
        coEvery { toggleFavorite(any()) } returns true
    }

    private fun `eventPublisher publishes event`() {
        every { eventPublisher.publish(any()) } returns Unit
    }

    private fun `eventPublisher publishes retained event`() {
        every { eventPublisher.publish(any(), retain = any()) } returns Unit
    }

    private val toggleFavorite = mockk<ToggleFavoriteInteractor>()
    private val eventPublisher = mockk<BrokerPublisher>()

    private val stateMachine: StateMachine<HomeAction, HomeState> by lazy {
        spyk(HomeViewModel(toggleFavorite, eventPublisher, TestCoroutineDispatcher()))
    }

    private val sampleWallpaper = Wallpaper(
        id = 1,
        name = "Card",
        artist = "Someone",
        color = "#000000",
        side = FRONT
    )
}
