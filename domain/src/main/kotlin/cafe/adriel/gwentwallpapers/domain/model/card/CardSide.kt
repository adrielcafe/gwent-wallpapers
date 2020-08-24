package cafe.adriel.gwentwallpapers.domain.model.card

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

enum class CardSide(override val title: String) : AdapterOption<CardSide> {
    ALL("All"),
    FRONT("Front"),
    BACK("Back")
}
