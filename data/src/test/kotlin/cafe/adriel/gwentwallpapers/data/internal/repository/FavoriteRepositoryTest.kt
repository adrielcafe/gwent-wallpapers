package cafe.adriel.gwentwallpapers.data.internal.repository

import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide
import cafe.adriel.satchel.SatchelStorage
import cafe.adriel.satchel.ktx.getOrDefault
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class FavoriteRepositoryTest {

    @Nested
    inner class GetFavorites {

        @Test
        fun `when storage has favorites then return all`() = runBlockingTest {
            `storage has favorites`()

            val result = repository.getFavorites()

            expectThat(result) containsExactly storedFavorites
        }

        @Test
        fun `when storage has no favorites then return empty`() = runBlockingTest {
            `storage is empty`()

            val result = repository.getFavorites()

            expectThat(result) containsExactly emptyFavorites
        }
    }

    @Nested
    inner class IsFavorite {

        @Test
        fun `when storage contains wallpaper then return true`() = runBlockingTest {
            `storage has favorites`()

            val result = repository.isFavorite(sampleWallpaper.copy(id = 1))

            expectThat(result).isTrue()
        }

        @Test
        fun `when storage does not contains wallpaper then return false`() = runBlockingTest {
            `storage has favorites`()

            val result = repository.isFavorite(sampleWallpaper.copy(id = 5))

            expectThat(result).isFalse()
        }
    }

    @Nested
    inner class ToggleFavorite {

        @Test
        fun `when wallpaper is favorite then return false`() = runBlockingTest {
            `storage has favorites`()
            `storage set value`()

            val result = repository.toggleFavorite(sampleWallpaper.copy(id = 2))

            expectThat(result).isFalse()
        }

        @Test
        fun `when wallpaper is not favorite then return true`() = runBlockingTest {
            `storage has favorites`()
            `storage set value`()

            val result = repository.toggleFavorite(sampleWallpaper.copy(id = 4))

            expectThat(result).isTrue()
        }
    }

    private fun `storage has favorites`() {
        every { storage.getOrDefault(FavoriteRepository.KEY_FAVORITES, emptySet<Int>()) } returns storedFavorites
    }

    private fun `storage is empty`() {
        every { storage.getOrDefault(FavoriteRepository.KEY_FAVORITES, emptySet<Int>()) } returns emptyFavorites
    }

    private fun `storage set value`() {
        every { storage.set<Int>(FavoriteRepository.KEY_FAVORITES, any()) } returns Unit
    }

    private val storage = mockk<SatchelStorage>()

    private val repository = FavoriteRepository(storage)

    private val storedFavorites = setOf(1, 2, 3)

    private val emptyFavorites = emptySet<Int>()

    private val sampleWallpaper = Wallpaper(
        id = 0,
        name = null,
        artist = null,
        color = "#000000",
        side = CardSide.FRONT
    )
}
