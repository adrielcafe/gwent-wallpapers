package cafe.adriel.gwentwallpapers.data

import cafe.adriel.gwentwallpapers.data.internal.repository.FavoriteRepository
import cafe.adriel.gwentwallpapers.data.internal.repository.SettingsRepository
import cafe.adriel.gwentwallpapers.data.internal.repository.WallpaperRepository
import cafe.adriel.gwentwallpapers.domain.model.AppInfo
import cafe.adriel.gwentwallpapers.shared.di.InjectionQualifier.Interactor
import cafe.adriel.gwentwallpapers.shared.di.InjectionQualifier.Storage
import cafe.adriel.satchel.Satchel
import cafe.adriel.satchel.serializer.gzip.GzipSatchelSerializer
import cafe.adriel.satchel.storer.file.FileSatchelStorer
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {

    // Storages
    single(named<Storage.Cards>()) {
        Satchel.with(
            storer = FileSatchelStorer(get<AppInfo>().cardsStorageFile),
            serializer = GzipSatchelSerializer
        )
    }

    single(named<Storage.Favorites>()) {
        Satchel.with(
            storer = FileSatchelStorer(get<AppInfo>().favoritesStorageFile),
            serializer = GzipSatchelSerializer
        )
    }

    single(named<Storage.Settings>()) {
        Satchel.with(
            storer = FileSatchelStorer(get<AppInfo>().settingsStorageFile),
            serializer = GzipSatchelSerializer
        )
    }

    // Repositories
    single {
        WallpaperRepository(
            storage = get(named<Storage.Cards>()),
            getFavorites = get(named<Interactor.GetFavorites>())
        )
    }

    single {
        FavoriteRepository(
            storage = get(named<Storage.Favorites>())
        )
    }

    single {
        SettingsRepository(
            storage = get(named<Storage.Settings>())
        )
    }

    // Interactors
    single(named<Interactor.GetRandomWallpaper>()) {
        get<WallpaperRepository>().getRandomWallpaper
    }

    single(named<Interactor.SearchWallpapers>()) {
        get<WallpaperRepository>().searchWallpapers
    }

    single(named<Interactor.GetFavorites>()) {
        get<FavoriteRepository>().getFavorites
    }

    single(named<Interactor.IsFavorite>()) {
        get<FavoriteRepository>().isFavorite
    }

    single(named<Interactor.ToggleFavorite>()) {
        get<FavoriteRepository>().toggleFavorite
    }

    single(named<Interactor.GetSettings>()) {
        get<SettingsRepository>().getSettings
    }

    single(named<Interactor.UpdateSettings>()) {
        get<SettingsRepository>().updateSettings
    }
}
