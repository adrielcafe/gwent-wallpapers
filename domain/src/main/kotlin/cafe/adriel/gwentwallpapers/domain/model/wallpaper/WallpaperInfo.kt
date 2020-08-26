package cafe.adriel.gwentwallpapers.domain.model.wallpaper

data class WallpaperInfo(
    val name: String,
    val flavor: String?,
    val info: String?,
    val artist: String?,
    val tags: List<String>
)
