package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.wallpapers

import cafe.adriel.broker.BrokerPublisher
import cafe.adriel.gwentwallpapers.domain.interactor.SearchWallpapersInteractor
import cafe.adriel.gwentwallpapers.domain.interactor.ToggleFavoriteInteractor
import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide.FRONT
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter
import cafe.adriel.gwentwallpapers.presentation.internal.model.WallpaperEvent
import cafe.adriel.hal.HAL.StateMachine
import cafe.adriel.hal.currentState
import cafe.adriel.hal.plusAssign
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class WallpapersStateMachineTest {

    @BeforeEach
    fun init() {
        `searchWallpapers returns wallpapers`()
        `toggleFavorite returns new status`()
        `eventPublisher publishes event`()
    }

    @Nested
    inner class SearchRequested {

        @Test
        fun `should search for wallpapers and update state`() {
            stateMachine += WallpapersAction.Search(SearchFilter(), SearchSorter.Random)

            coVerify { searchWallpapers(SearchFilter(), SearchSorter.Random) }

            expectThat(stateMachine.currentState) isEqualTo WallpapersState(
                wallpapers = listOf(sampleWallpaper), loading = false
            )
        }
    }

    @Nested
    inner class ToggleFavorite {

        @Test
        fun `should toggle favorite and publish event`() {
            stateMachine += WallpapersAction.ToggleFavorite(sampleWallpaper)

            coVerifySequence {
                toggleFavorite(sampleWallpaper)
                eventPublisher.publish(WallpaperEvent.FavoriteChanged(sampleWallpaper, true))
            }

            expectThat(stateMachine.currentState) isEqualTo WallpapersState()
        }
    }

    private fun `searchWallpapers returns wallpapers`() {
        coEvery { searchWallpapers(any(), any()) } returns listOf(sampleWallpaper)
    }

    private fun `toggleFavorite returns new status`() {
        coEvery { toggleFavorite(any()) } returns true
    }

    private fun `eventPublisher publishes event`() {
        every { eventPublisher.publish(any()) } returns Unit
    }

    private val searchWallpapers = mockk<SearchWallpapersInteractor>()
    private val toggleFavorite = mockk<ToggleFavoriteInteractor>()
    private val eventPublisher = mockk<BrokerPublisher>()

    private val stateMachine: StateMachine<WallpapersAction, WallpapersState> by lazy {
        spyk(WallpapersViewModel(searchWallpapers, toggleFavorite, eventPublisher, TestCoroutineDispatcher()))
    }

    private val sampleWallpaper = Wallpaper(
        id = 1,
        name = "Card",
        artist = "Someone",
        color = "#000000",
        side = FRONT
    )
}
