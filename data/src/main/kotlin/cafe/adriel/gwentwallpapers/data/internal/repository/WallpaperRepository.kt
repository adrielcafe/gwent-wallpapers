package cafe.adriel.gwentwallpapers.data.internal.repository

import cafe.adriel.gwentwallpapers.data.internal.ktx.filterCardBacks
import cafe.adriel.gwentwallpapers.data.internal.ktx.filterCardFronts
import cafe.adriel.gwentwallpapers.data.internal.ktx.sortWallpapers
import cafe.adriel.gwentwallpapers.data.internal.mapper.toWallpaper
import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardBack
import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardFront
import cafe.adriel.gwentwallpapers.domain.interactor.GetFavoritesInteractor
import cafe.adriel.gwentwallpapers.domain.interactor.GetRandomWallpaperInteractor
import cafe.adriel.gwentwallpapers.domain.interactor.SearchWallpapersInteractor
import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.card.CardFavorite
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter
import cafe.adriel.satchel.SatchelStorage
import cafe.adriel.satchel.ktx.value
import java.util.IdentityHashMap

internal class WallpaperRepository(
    storage: SatchelStorage,
    private val getFavorites: GetFavoritesInteractor
) {

    companion object {
        const val KEY_CARDS_FRONT = "cards.front"
        const val KEY_CARDS_BACK = "cards.back"
    }

    val getRandomWallpaper: GetRandomWallpaperInteractor = { onlyFavorites ->
        searchWallpapers(
            when (onlyFavorites) {
                true -> SearchFilter(favorite = CardFavorite.YES)
                false -> SearchFilter()
            },
            SearchSorter.Random
        ).randomOrNull()
    }

    val searchWallpapers: SearchWallpapersInteractor = { filter, sorter ->
        searchCardFronts(filter)
            .plus(searchCardBacks(filter))
            .sortWallpapers(sorter)
            .toList()
    }

    private val cardFronts by storage.value(KEY_CARDS_FRONT, emptyList<GwentCardFront>())

    private val cardBacks by storage.value(KEY_CARDS_BACK, emptyList<GwentCardBack>())

    private val mappedWallpapers by lazy {
        IdentityHashMap<Int, Wallpaper>(cardFronts.count() + cardBacks.count())
    }

    private suspend fun searchCardFronts(filter: SearchFilter): Sequence<Wallpaper> =
        cardFronts
            .asSequence()
            .filterCardFronts(filter, getFavorites())
            .map { card ->
                mappedWallpapers[card.artId]
                    ?: card.toWallpaper().also { mappedWallpapers[card.artId] = it }
            }

    private suspend fun searchCardBacks(filter: SearchFilter): Sequence<Wallpaper> =
        cardBacks
            .asSequence()
            .filterCardBacks(filter, getFavorites())
            .map { card ->
                mappedWallpapers[card.id]
                    ?: card.toWallpaper().also { mappedWallpapers[card.id] = it }
            }
}
