package cafe.adriel.gwentwallpapers

import cafe.adriel.gwentwallpapers.domain.model.AppInfo
import java.io.File
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val MARKET_URI = "market://details?id=${BuildConfig.APPLICATION_ID}"
private const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"

private const val PROVIDER_AUTHORITY = "${BuildConfig.APPLICATION_ID}.provider"

private const val STORAGE_CARDS = "cards-7.1.db"
private const val STORAGE_FAVORITES = "favorites.db"
private const val STORAGE_SETTINGS = "settings.db"

val appModule = module {

    single {
        AppInfo(
            version = BuildConfig.VERSION_NAME,
            releaseBuild = BuildConfig.RELEASE,
            marketUri = MARKET_URI,
            playStoreUrl = PLAY_STORE_URL,
            fileProviderAuthority = PROVIDER_AUTHORITY,
            cardsStorageFile = File(androidContext().filesDir, STORAGE_CARDS),
            favoritesStorageFile = File(androidContext().filesDir, STORAGE_FAVORITES),
            settingsStorageFile = File(androidContext().filesDir, STORAGE_SETTINGS)
        )
    }
}
