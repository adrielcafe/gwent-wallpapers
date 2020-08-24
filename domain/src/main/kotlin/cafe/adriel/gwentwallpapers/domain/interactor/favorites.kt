package cafe.adriel.gwentwallpapers.domain.interactor

import cafe.adriel.gwentwallpapers.domain.model.Wallpaper

private typealias WallpaperId = Int

typealias GetFavoritesInteractor = suspend () -> Set<WallpaperId>
typealias IsFavoriteInteractor = suspend (Wallpaper) -> Boolean
typealias ToggleFavoriteInteractor = suspend (Wallpaper) -> Boolean
