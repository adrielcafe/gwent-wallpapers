package cafe.adriel.gwentwallpapers.data.internal.model

import cafe.adriel.gwentwallpapers.domain.model.card.CardColor
import cafe.adriel.gwentwallpapers.domain.model.card.CardFaction
import cafe.adriel.gwentwallpapers.domain.model.card.CardRarity
import cafe.adriel.gwentwallpapers.domain.model.card.CardSet
import cafe.adriel.gwentwallpapers.domain.model.card.CardType
import java.io.Serializable

internal data class GwentCardFront(
    val id: Int,
    val artId: Int,
    val name: String,
    val faction: CardFaction,
    val set: CardSet,
    val type: CardType,
    val rarity: CardRarity,
    val color: CardColor,
    val primaryColor: String,
    val flavor: String? = null,
    val info: String? = null,
    val artist: String? = null
) : Serializable {

    companion object {
        @JvmStatic private val serialVersionUID: Long = 0
    }
}
