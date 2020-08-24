package cafe.adriel.gwentwallpapers.data.internal.repository

import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardBack
import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardFront
import cafe.adriel.gwentwallpapers.domain.interactor.GetFavoritesInteractor
import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.card.CardColor.BRONZE
import cafe.adriel.gwentwallpapers.domain.model.card.CardFaction.NEUTRAL
import cafe.adriel.gwentwallpapers.domain.model.card.CardRarity.COMMON
import cafe.adriel.gwentwallpapers.domain.model.card.CardSet.STARTER
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide
import cafe.adriel.gwentwallpapers.domain.model.card.CardType.UNIT
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter
import cafe.adriel.satchel.SatchelStorage
import cafe.adriel.satchel.ktx.getOrDefault
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.containsExactly
import strikt.assertions.isNotNull
import strikt.assertions.isNull

@TestInstance(Lifecycle.PER_CLASS)
class WallpaperRepositoryTest {

    @BeforeEach
    fun init() {
        `storage returns card fronts`()
        `storage returns card backs`()
    }

    @Nested
    inner class GetRandomWallpaper {

        @Test
        fun `when get any wallpaper then return a wallpaper`() = runBlockingTest {
            `getFavorites has favorites`()

            val result = repository.getRandomWallpaper(false)

            expectThat(result).isNotNull()
        }

        @Test
        fun `when get only favorite wallpaper then return a favorite wallpaper`() = runBlockingTest {
            `getFavorites has favorites`()

            val result = repository.getRandomWallpaper(true)

            expectThat(result).isNotNull()
            expectThat(favorites) contains setOf(result!!.id)
        }

        @Test
        fun `when get only favorite wallpaper but there is no favorite then return null`() = runBlockingTest {
            `getFavorites is empty`()

            val result = repository.getRandomWallpaper(true)

            expectThat(result).isNull()
        }
    }

    @Nested
    inner class SearchWallpapers {

        @Test
        fun `when sort by random then return sorted wallpapers`() = runBlockingTest {
            `getFavorites has favorites`()

            val result = repository.searchWallpapers(SearchFilter(), SearchSorter.Random)

            expectThat(result) contains wallpapers
        }

        @Test
        fun `when sort by name ascending then return sorted wallpapers`() = runBlockingTest {
            `getFavorites has favorites`()

            val result = repository.searchWallpapers(SearchFilter(), SearchSorter.Name(ascending = true))

            expectThat(result) containsExactly sortedAscendingWallpapers
        }

        @Test
        fun `when sort by name descending then return sorted wallpapers`() = runBlockingTest {
            `getFavorites has favorites`()

            val result = repository.searchWallpapers(SearchFilter(), SearchSorter.Name(ascending = false))

            expectThat(result) containsExactly sortedDescendingWallpapers
        }
    }

    private fun `storage returns card fronts`() {
        every {
            storage.getOrDefault(WallpaperRepository.KEY_CARDS_FRONT, emptyList<GwentCardFront>())
        } returns listOf(sampleCardFront)
    }

    private fun `storage returns card backs`() {
        every {
            storage.getOrDefault(WallpaperRepository.KEY_CARDS_BACK, emptyList<GwentCardBack>())
        } returns listOf(sampleCardBack)
    }

    private fun `getFavorites has favorites`() {
        coEvery { getFavorites() } returns favorites
    }

    private fun `getFavorites is empty`() {
        coEvery { getFavorites() } returns emptySet()
    }

    private val storage = mockk<SatchelStorage>()

    private val getFavorites = mockk<GetFavoritesInteractor>()

    private val repository by lazy { WallpaperRepository(storage, getFavorites) }

    private val favorites = setOf(1, 2)

    private val sampleCardFront = GwentCardFront(
        id = 0,
        artId = 1,
        name = "Card",
        faction = NEUTRAL,
        set = STARTER,
        type = UNIT,
        rarity = COMMON,
        color = BRONZE,
        primaryColor = "#000000",
        artist = "Someone"
    )

    private val sampleCardBack = GwentCardBack(
        id = 2,
        primaryColor = "#FFFFFF"
    )

    private val wallpapers = listOf(
        Wallpaper(
            id = 1,
            name = "Card",
            artist = "Someone",
            color = "#000000",
            side = CardSide.FRONT
        ),
        Wallpaper(
            id = 2,
            name = null,
            artist = null,
            color = "#FFFFFF",
            side = CardSide.BACK
        )
    )

    private val sortedAscendingWallpapers = wallpapers.sortedBy { it.name }

    private val sortedDescendingWallpapers = wallpapers.sortedByDescending { it.name }
}
