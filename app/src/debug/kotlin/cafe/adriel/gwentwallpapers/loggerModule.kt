package cafe.adriel.gwentwallpapers

import cafe.adriel.gwentwallpapers.internal.AndroidLogger
import cafe.adriel.gwentwallpapers.shared.logger.Logger
import org.koin.dsl.module

val loggerModule = module {

    single<Logger> {
        AndroidLogger
    }
}
