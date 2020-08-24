package cafe.adriel.gwentwallpapers.domain.interactor

import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter

private typealias OnlyFavorites = Boolean

typealias GetRandomWallpaperInteractor = suspend (OnlyFavorites) -> Wallpaper?
typealias SearchWallpapersInteractor = suspend (SearchFilter, SearchSorter) -> List<Wallpaper>
