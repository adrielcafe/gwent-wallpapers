package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen

import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter
import cafe.adriel.gwentwallpapers.presentation.internal.model.ScreenSection
import cafe.adriel.hal.HAL

internal sealed class HomeAction : HAL.Action {
    object Init : HomeAction()
    data class Navigate(val section: ScreenSection) : HomeAction()
    data class UpdateSearchFilter(val filter: SearchFilter) : HomeAction()
    data class UpdateSearchSorter(val sorter: SearchSorter) : HomeAction()
    data class ToggleFavorite(val wallpaper: Wallpaper) : HomeAction()
}

internal data class HomeState(
    val section: ScreenSection = ScreenSection.WALLPAPERS,
    val filter: SearchFilter = SearchFilter(),
    val sorter: SearchSorter = SearchSorter.Random
) : HAL.State
