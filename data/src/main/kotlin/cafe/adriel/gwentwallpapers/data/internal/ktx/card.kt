package cafe.adriel.gwentwallpapers.data.internal.ktx

import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardBack
import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardFront
import cafe.adriel.gwentwallpapers.domain.model.card.CardColor
import cafe.adriel.gwentwallpapers.domain.model.card.CardFaction
import cafe.adriel.gwentwallpapers.domain.model.card.CardFavorite
import cafe.adriel.gwentwallpapers.domain.model.card.CardRarity
import cafe.adriel.gwentwallpapers.domain.model.card.CardSet
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide
import cafe.adriel.gwentwallpapers.domain.model.card.CardType
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.shared.ktx.matches

internal fun Sequence<GwentCardFront>.filterCardFronts(
    filter: SearchFilter,
    favorites: Set<Int>
): Sequence<GwentCardFront> =
    filter { card ->
        when (filter.name.isBlank()) {
            true -> true
            false -> card.name.matches(filter.name)
        }
    }.filter { card ->
        when (filter.favorite) {
            CardFavorite.ALL -> true
            CardFavorite.YES -> card.artId in favorites
            CardFavorite.NO -> card.artId !in favorites
        }
    }.filter {
        when (filter.side) {
            CardSide.ALL,
            CardSide.FRONT -> true
            else -> false
        }
    }.filter { card ->
        when (filter.faction) {
            CardFaction.ALL -> true
            else -> card.faction == filter.faction
        }
    }.filter { card ->
        when (filter.set) {
            CardSet.ALL -> true
            else -> card.set == filter.set
        }
    }.filter { card ->
        when (filter.type) {
            CardType.ALL -> true
            else -> card.type == filter.type
        }
    }.filter { card ->
        when (filter.rarity) {
            CardRarity.ALL -> true
            else -> card.rarity == filter.rarity
        }
    }.filter { card ->
        when (filter.color) {
            CardColor.ALL -> true
            else -> card.color == filter.color
        }
    }

internal fun Sequence<GwentCardBack>.filterCardBacks(
    filter: SearchFilter,
    favorites: Set<Int>
): Sequence<GwentCardBack> =
    filter { filter.name.isBlank() }
        .filter { card ->
            when (filter.favorite) {
                CardFavorite.ALL -> true
                CardFavorite.YES -> card.id in favorites
                CardFavorite.NO -> card.id !in favorites
            }
        }
        .filter {
            when (filter.side) {
                CardSide.ALL,
                CardSide.BACK -> true
                CardSide.FRONT -> false
            }
        }
        .filter { filter.faction == CardFaction.ALL }
        .filter { filter.set == CardSet.ALL }
        .filter { filter.type == CardType.ALL }
        .filter { filter.rarity == CardRarity.ALL }
        .filter { filter.color == CardColor.ALL }
