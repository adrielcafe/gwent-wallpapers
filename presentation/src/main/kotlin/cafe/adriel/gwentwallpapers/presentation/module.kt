package cafe.adriel.gwentwallpapers.presentation

import android.app.WallpaperManager
import android.content.ClipboardManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.work.WorkManager
import cafe.adriel.broker.Broker
import cafe.adriel.broker.BrokerPublisher
import cafe.adriel.broker.BrokerSubscriber
import cafe.adriel.gwentwallpapers.domain.model.settings.Theme
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.styleId
import cafe.adriel.gwentwallpapers.presentation.internal.ui.HomeActivity
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.WallpaperModelItem
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.ConnectivityHelper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.ImageLoader
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.ImageLoaderHelper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.NotificationHelper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.WallpaperExpanderHelper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.WallpaperHelper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.HomeViewModel
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsViewModel
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.wallpapers.WallpapersViewModel
import cafe.adriel.gwentwallpapers.shared.di.InjectionQualifier.Dispatcher
import cafe.adriel.gwentwallpapers.shared.di.InjectionQualifier.Interactor
import com.bumptech.glide.Glide
import daio.io.dresscode.DressCode
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

typealias Themes = Array<DressCode>

val presentationModule = module {

    scope<HomeActivity> {

        // ViewModels
        viewModel {
            HomeViewModel(
                toggleFavorite = get(named<Interactor.ToggleFavorite>()),
                eventPublisher = get(),
                dispatcher = get(named<Dispatcher.ViewModel>())
            )
        }

        viewModel {
            WallpapersViewModel(
                searchWallpapers = get(named<Interactor.SearchWallpapers>()),
                toggleFavorite = get(named<Interactor.ToggleFavorite>()),
                eventPublisher = get(),
                dispatcher = get(named<Dispatcher.ViewModel>())
            )
        }

        viewModel {
            SettingsViewModel(
                getSettings = get(named<Interactor.GetSettings>()),
                updateSettings = get(named<Interactor.UpdateSettings>()),
                workManager = get(),
                dispatcher = get(named<Dispatcher.ViewModel>())
            )
        }

        // PubSub
        val broker = Broker()

        scoped<BrokerPublisher> {
            broker
        }

        scoped<BrokerSubscriber> {
            broker
        }

        // Helpers
        scoped {
            ConnectivityHelper(
                lifecycle = getSource<AppCompatActivity>().lifecycle,
                manager = get()
            )
        }

        scoped {
            WallpaperExpanderHelper(
                eventPublisher = get()
            )
        }

        factory { (getItem: (position: Int) -> WallpaperModelItem) ->
            ImageLoaderHelper(
                imageLoader = get(),
                getItem = getItem
            )
        }
    }

    // Helpers
    single {
        WallpaperHelper(
            context = androidContext(),
            wallpaperManager = get(),
            clipboardManager = get(),
            imageLoader = get(),
            appInfo = get(),
            dispatcher = get(named<Dispatcher.Helper>()),
            logger = get()
        )
    }

    single {
        NotificationHelper(
            context = androidContext()
        )
    }

    // Third-party helpers
    single<Themes> {
        Theme.values()
            .map { DressCode(it.name, it.styleId) }
            .toTypedArray()
    }

    single<ImageLoader> {
        Glide.with(androidContext())
    }

    // Managers
    single {
        WorkManager.getInstance(androidContext())
    }

    single {
        WallpaperManager.getInstance(androidContext())
    }

    single {
        androidContext().getSystemService<ClipboardManager>()!!
    }

    single {
        androidContext().getSystemService<ConnectivityManager>()!!
    }
}
