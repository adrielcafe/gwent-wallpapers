package cafe.adriel.gwentwallpapers.domain.model.search

import cafe.adriel.gwentwallpapers.domain.model.card.CardColor
import cafe.adriel.gwentwallpapers.domain.model.card.CardFaction
import cafe.adriel.gwentwallpapers.domain.model.card.CardFavorite
import cafe.adriel.gwentwallpapers.domain.model.card.CardRarity
import cafe.adriel.gwentwallpapers.domain.model.card.CardSet
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide
import cafe.adriel.gwentwallpapers.domain.model.card.CardType

data class SearchFilter(
    val name: String = "",
    val favorite: CardFavorite = CardFavorite.ALL,
    val side: CardSide = CardSide.ALL,
    val color: CardColor = CardColor.ALL,
    val rarity: CardRarity = CardRarity.ALL,
    val type: CardType = CardType.ALL,
    val faction: CardFaction = CardFaction.ALL,
    val set: CardSet = CardSet.ALL
) {

    val count = run {
        var counter = 0
        if (name.isNotBlank()) counter++
        if (favorite != CardFavorite.ALL) counter++
        if (side != CardSide.ALL) counter++
        if (color != CardColor.ALL) counter++
        if (rarity != CardRarity.ALL) counter++
        if (type != CardType.ALL) counter++
        if (faction != CardFaction.ALL) counter++
        if (set != CardSet.ALL) counter++
        counter
    }
}
