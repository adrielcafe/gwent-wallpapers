package cafe.adriel.gwentwallpapers.shared

import cafe.adriel.gwentwallpapers.shared.di.InjectionQualifier.Dispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sharedModule = module {

    // Dispatchers
    single(named<Dispatcher.ViewModel>()) {
        Dispatchers.IO
    }

    single(named<Dispatcher.Worker>()) {
        Dispatchers.IO
    }

    single(named<Dispatcher.Helper>()) {
        Dispatchers.IO
    }
}
