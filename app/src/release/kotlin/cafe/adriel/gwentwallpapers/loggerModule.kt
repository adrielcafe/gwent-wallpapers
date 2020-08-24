package cafe.adriel.gwentwallpapers

import cafe.adriel.gwentwallpapers.internal.CrashlyticsLogger
import cafe.adriel.gwentwallpapers.shared.logger.Logger
import org.koin.dsl.module

val loggerModule = module {

    single<Logger> {
        CrashlyticsLogger
    }
}
