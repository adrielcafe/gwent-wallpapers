package cafe.adriel.gwentwallpapers.domain.model.card

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

enum class CardColor(override val title: String) : AdapterOption<CardColor> {
    ALL("All"),
    BRONZE("Bronze"),
    GOLD("Gold")
}
