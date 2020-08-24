package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.wallpapers

import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter
import cafe.adriel.hal.HAL

internal sealed class WallpapersAction : HAL.Action {
    data class Search(val filter: SearchFilter, val sorter: SearchSorter) : WallpapersAction()
    data class ToggleFavorite(val wallpaper: Wallpaper) : WallpapersAction()
}

internal data class WallpapersState(
    val wallpapers: List<Wallpaper> = emptyList(),
    val loading: Boolean = true
) : HAL.State
