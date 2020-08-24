package cafe.adriel.gwentwallpapers.internal

import cafe.adriel.gwentwallpapers.shared.logger.Logger
import com.google.firebase.crashlytics.FirebaseCrashlytics

internal object CrashlyticsLogger : Logger {

    override fun d(message: String) {
        FirebaseCrashlytics.getInstance().log("DEBUG$tag: $message")
    }

    override fun w(message: String, exception: Throwable?) {
        FirebaseCrashlytics.getInstance().apply {
            log("WARNING$tag: $message")
            exception?.let(::recordException)
        }
    }

    override fun e(message: String, exception: Throwable) {
        FirebaseCrashlytics.getInstance().apply {
            log("ERROR$tag: $message")
            recordException(exception)
        }
    }
}
