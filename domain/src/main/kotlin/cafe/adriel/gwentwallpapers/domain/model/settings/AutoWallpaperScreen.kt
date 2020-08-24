package cafe.adriel.gwentwallpapers.domain.model.settings

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

enum class AutoWallpaperScreen(
    override val title: String
) : AdapterOption<AutoWallpaperScreen> {
    BOTH("Both screens"),
    HOME("Home screen"),
    LOCK("Lock screen")
}
