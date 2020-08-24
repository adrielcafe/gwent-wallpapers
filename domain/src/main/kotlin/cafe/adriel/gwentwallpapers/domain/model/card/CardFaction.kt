package cafe.adriel.gwentwallpapers.domain.model.card

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

enum class CardFaction(override val title: String) : AdapterOption<CardFaction> {
    ALL("All"),
    NEUTRAL("Neutral"),
    MONSTERS("Monsters"),
    NILFGAARD("Nilfgaard"),
    NORTHERN_REALMS("Northern Realms"),
    SCOIATAEL("Scoia'tael"),
    SKELLIGE("Skellige"),
    SYNDICATE("Syndicate")
}
