package cafe.adriel.gwentwallpapers.domain.model.card

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

enum class CardType(override val title: String) : AdapterOption<CardType> {
    ALL("All"),
    UNIT("Unit"),
    ARTIFACT("Artifact"),
    SPECIAL("Special"),
    STRATAGEM("Stratagem"),
    LEADER("Leader")
}
