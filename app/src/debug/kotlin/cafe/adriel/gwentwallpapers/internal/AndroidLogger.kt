package cafe.adriel.gwentwallpapers.internal

import android.util.Log
import cafe.adriel.gwentwallpapers.shared.logger.Logger

internal object AndroidLogger : Logger {

    override fun d(message: String) {
        Log.d(tag, message)
    }

    override fun w(message: String, exception: Throwable?) {
        Log.w(tag, message, exception)
    }

    override fun e(message: String, exception: Throwable) {
        Log.e(tag, message, exception)
    }
}
