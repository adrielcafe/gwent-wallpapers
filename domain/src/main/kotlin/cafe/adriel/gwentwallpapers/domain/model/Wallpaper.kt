package cafe.adriel.gwentwallpapers.domain.model

import cafe.adriel.gwentwallpapers.domain.model.card.CardSide

data class Wallpaper(
    val id: Int,
    val name: String?,
    val artist: String?,
    val color: String,
    val side: CardSide
)
