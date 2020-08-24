package cafe.adriel.gwentwallpapers.shared.logger

import cafe.adriel.gwentwallpapers.shared.internal.ktx.currentClassName

interface Logger {

    val tag: String
        get() = "[$currentClassName]"

    fun d(message: String)

    fun w(message: String, exception: Throwable? = null)

    fun e(message: String, exception: Throwable)
}
