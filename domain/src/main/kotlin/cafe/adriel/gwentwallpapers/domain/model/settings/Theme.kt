package cafe.adriel.gwentwallpapers.domain.model.settings

import cafe.adriel.gwentwallpapers.domain.model.card.CardFaction
import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

enum class Theme(override val title: String) : AdapterOption<Theme> {
    DEFAULT("Default"),
    NEUTRAL(CardFaction.NEUTRAL.title),
    MONSTERS(CardFaction.MONSTERS.title),
    NILFGAARD(CardFaction.NILFGAARD.title),
    NORTHERN_REALMS(CardFaction.NORTHERN_REALMS.title),
    SCOIATAEL(CardFaction.SCOIATAEL.title),
    SKELLIGE(CardFaction.SKELLIGE.title),
    SYNDICATE(CardFaction.SYNDICATE.title)
}
