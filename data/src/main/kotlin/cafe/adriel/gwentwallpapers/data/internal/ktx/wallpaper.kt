package cafe.adriel.gwentwallpapers.data.internal.ktx

import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter
import cafe.adriel.gwentwallpapers.domain.model.wallpaper.Wallpaper
import cafe.adriel.gwentwallpapers.shared.ktx.normalized

internal fun Sequence<Wallpaper>.sortWallpapers(sorter: SearchSorter): Sequence<Wallpaper> =
    when (sorter) {
        is SearchSorter.Random -> shuffled()
        is SearchSorter.Name -> when (sorter.ascending) {
            true -> sortedBy { it.name?.normalized }
            false -> sortedByDescending { it.name?.normalized }
        }
    }
