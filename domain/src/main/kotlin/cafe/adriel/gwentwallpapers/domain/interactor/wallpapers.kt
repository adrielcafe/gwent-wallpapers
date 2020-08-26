package cafe.adriel.gwentwallpapers.domain.interactor

import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter
import cafe.adriel.gwentwallpapers.domain.model.wallpaper.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.wallpaper.WallpaperInfo

private typealias OnlyFavorites = Boolean

typealias GetRandomWallpaperInteractor = suspend (OnlyFavorites) -> Wallpaper?
typealias GetWallpaperInfoInteractor = suspend (Wallpaper) -> WallpaperInfo
typealias SearchWallpapersInteractor = suspend (SearchFilter, SearchSorter) -> List<Wallpaper>
