package cafe.adriel.gwentwallpapers.shared.di

sealed class InjectionQualifier {

    sealed class Dispatcher {
        object ViewModel : Dispatcher()
        object Worker : Dispatcher()
        object Helper : Dispatcher()
    }

    sealed class Storage {
        object Cards : Storage()
        object Favorites : Storage()
        object Settings : Storage()
    }

    sealed class Interactor {
        // Wallpapers
        object GetRandomWallpaper : Interactor()
        object SearchWallpapers : Interactor()

        // Favorites
        object GetFavorites : Interactor()
        object IsFavorite : Interactor()
        object ToggleFavorite : Interactor()

        // Settings
        object GetSettings : Interactor()
        object UpdateSettings : Interactor()
    }
}
