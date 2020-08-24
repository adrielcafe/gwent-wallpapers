package cafe.adriel.gwentwallpapers.domain.model.settings

data class Settings(
    val theme: Theme = Theme.DEFAULT,
    val autoWallpaper: AutoWallpaper = AutoWallpaper()
)
