package cafe.adriel.gwentwallpapers.presentation.internal.model

import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter

internal sealed class WallpaperEvent {
    data class SearchRequested(val filter: SearchFilter, val sorter: SearchSorter) : WallpaperEvent()
    data class FavoriteChanged(val wallpaper: Wallpaper, val favorite: Boolean) : WallpaperEvent()

    sealed class WallpaperExpander {
        data class Expanded(val wallpaper: Wallpaper) : WallpaperExpander()
        object Shrinked : WallpaperExpander()
    }
}
