package cafe.adriel.gwentwallpapers.data.internal.mapper

import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardBack
import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardFront
import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide

internal fun GwentCardFront.toWallpaper(): Wallpaper =
    Wallpaper(
        id = artId,
        name = name,
        artist = artist,
        color = primaryColor,
        side = CardSide.FRONT
    )

internal fun GwentCardBack.toWallpaper(): Wallpaper =
    Wallpaper(
        id = id,
        name = null,
        artist = null,
        color = primaryColor,
        side = CardSide.BACK
    )
