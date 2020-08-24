package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.wallpapers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.broker.BrokerPublisher
import cafe.adriel.gwentwallpapers.domain.interactor.SearchWallpapersInteractor
import cafe.adriel.gwentwallpapers.domain.interactor.ToggleFavoriteInteractor
import cafe.adriel.gwentwallpapers.presentation.internal.model.WallpaperEvent
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.wallpapers.WallpapersAction.Search
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.wallpapers.WallpapersAction.ToggleFavorite
import cafe.adriel.hal.HAL
import cafe.adriel.hal.HAL.StateMachine
import kotlinx.coroutines.CoroutineDispatcher

internal class WallpapersViewModel(
    private val searchWallpapers: SearchWallpapersInteractor,
    private val toggleFavorite: ToggleFavoriteInteractor,
    private val eventPublisher: BrokerPublisher,
    dispatcher: CoroutineDispatcher
) : ViewModel(), StateMachine<WallpapersAction, WallpapersState> {

    override val stateMachine by HAL(WallpapersState(), viewModelScope, dispatcher) { action, state ->
        when (action) {
            is Search -> {
                +state.copy(loading = true)
                +state.copy(loading = false, wallpapers = searchWallpapers(action.filter, action.sorter))
            }

            is ToggleFavorite -> {
                val favorite = toggleFavorite(action.wallpaper)
                eventPublisher.publish(WallpaperEvent.FavoriteChanged(action.wallpaper, favorite))
            }
        }
    }
}
