package cafe.adriel.gwentwallpapers.domain.model.settings

data class AutoWallpaper(
    val enabled: Boolean = false,
    val showNotification: Boolean = true,
    val onlyFavorites: Boolean = false,
    val screen: AutoWallpaperScreen = AutoWallpaperScreen.BOTH,
    val frequency: AutoWallpaperFrequency = AutoWallpaperFrequency.EVERY_DAY
)
