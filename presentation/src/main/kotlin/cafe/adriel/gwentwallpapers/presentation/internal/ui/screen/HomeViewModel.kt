package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.broker.BrokerPublisher
import cafe.adriel.gwentwallpapers.domain.interactor.ToggleFavoriteInteractor
import cafe.adriel.gwentwallpapers.presentation.internal.model.WallpaperEvent
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.HomeAction.Init
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.HomeAction.Navigate
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.HomeAction.ToggleFavorite
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.HomeAction.UpdateSearchFilter
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.HomeAction.UpdateSearchSorter
import cafe.adriel.hal.HAL
import cafe.adriel.hal.HAL.StateMachine
import cafe.adriel.hal.emit
import kotlinx.coroutines.CoroutineDispatcher

internal class HomeViewModel(
    private val toggleFavorite: ToggleFavoriteInteractor,
    private val eventPublisher: BrokerPublisher,
    dispatcher: CoroutineDispatcher
) : ViewModel(), StateMachine<HomeAction, HomeState> {

    override val stateMachine by HAL(HomeState(), viewModelScope, dispatcher) { action, state ->
        when (action) {
            is Init -> eventPublisher.publish(WallpaperEvent.SearchRequested(state.filter, state.sorter), retain = true)

            is Navigate -> +state.copy(section = action.section)

            is UpdateSearchFilter -> {
                +state.copy(filter = action.filter)
                eventPublisher.publish(WallpaperEvent.SearchRequested(action.filter, state.sorter))
            }

            is UpdateSearchSorter -> {
                +state.copy(sorter = action.sorter)
                eventPublisher.publish(WallpaperEvent.SearchRequested(state.filter, action.sorter))
            }

            is ToggleFavorite -> {
                val favorite = toggleFavorite(action.wallpaper)
                eventPublisher.publish(WallpaperEvent.FavoriteChanged(action.wallpaper, favorite))
            }
        }
    }

    init {
        emit(Init)
    }
}
