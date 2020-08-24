package cafe.adriel.gwentwallpapers.shared.internal.ktx

private const val CURRENT_CLASS_INDEX = 3

internal val currentClassName: String
    get() = Throwable().stackTrace[CURRENT_CLASS_INDEX].className
        .split(".").last()
        .split("$").first()
