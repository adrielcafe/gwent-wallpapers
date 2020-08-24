package cafe.adriel.gwentwallpapers

import android.app.Application
import cafe.adriel.gwentwallpapers.data.dataModule
import cafe.adriel.gwentwallpapers.domain.model.AppInfo
import cafe.adriel.gwentwallpapers.presentation.Themes
import cafe.adriel.gwentwallpapers.presentation.presentationModule
import cafe.adriel.gwentwallpapers.shared.sharedModule
import daio.io.dresscode.declareDressCode
import java.io.File
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDI()
        setupStorage()
        setupThemes()
    }

    private fun setupDI() {
        startKoin {
            if (BuildConfig.RELEASE.not()) androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(appModule + loggerModule + sharedModule + dataModule + presentationModule)
        }
    }

    // Exports cards storage from raw resources to files directory to make it accessible to the data layer
    private fun setupStorage() {
        get<AppInfo>()
            .cardsStorageFile
            .takeUnless(File::exists)
            ?.writeBytes(resources.openRawResource(R.raw.cards).readBytes())
    }

    private fun setupThemes() {
        declareDressCode(*get<Themes>())
    }
}
