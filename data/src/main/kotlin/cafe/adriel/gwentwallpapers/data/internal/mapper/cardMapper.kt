package cafe.adriel.gwentwallpapers.data.internal.mapper

import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardBack
import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardFront
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide.BACK
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide.FRONT
import cafe.adriel.gwentwallpapers.domain.model.wallpaper.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.wallpaper.WallpaperInfo

internal fun GwentCardFront.toWallpaper(): Wallpaper =
    Wallpaper(
        id = artId,
        name = name,
        artist = artist,
        color = primaryColor,
        side = FRONT
    )

internal fun GwentCardBack.toWallpaper(): Wallpaper =
    Wallpaper(
        id = id,
        name = null,
        artist = null,
        color = primaryColor,
        side = BACK
    )

internal fun GwentCardFront.toWallpaperInfo(): WallpaperInfo =
    WallpaperInfo(
        name = name,
        flavor = flavor?.trim(),
        info = info?.trim(),
        artist = artist,
        tags = listOf(
            color.title,
            rarity.title,
            type.title,
            faction.title,
            set.title
        )
    )
