package cafe.adriel.gwentwallpapers.data.internal.repository

import cafe.adriel.gwentwallpapers.domain.interactor.GetFavoritesInteractor
import cafe.adriel.gwentwallpapers.domain.interactor.IsFavoriteInteractor
import cafe.adriel.gwentwallpapers.domain.interactor.ToggleFavoriteInteractor
import cafe.adriel.satchel.SatchelStorage
import cafe.adriel.satchel.ktx.value

internal class FavoriteRepository(storage: SatchelStorage) {

    companion object {
        const val KEY_FAVORITES = "favorites"
    }

    val getFavorites: GetFavoritesInteractor = {
        favorites
    }

    val isFavorite: IsFavoriteInteractor = { wallpaper ->
        favorites.contains(wallpaper.id)
    }

    val toggleFavorite: ToggleFavoriteInteractor = { wallpaper ->
        isFavorite(wallpaper)
            .not()
            .also { favorite ->
                favorites = when (favorite) {
                    true -> favorites + wallpaper.id
                    false -> favorites - wallpaper.id
                }
            }
    }

    private var favorites by storage.value(KEY_FAVORITES, emptySet<Int>())
}
