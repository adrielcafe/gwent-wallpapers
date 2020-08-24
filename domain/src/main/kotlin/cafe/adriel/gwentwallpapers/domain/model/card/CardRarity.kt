package cafe.adriel.gwentwallpapers.domain.model.card

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

enum class CardRarity(override val title: String) : AdapterOption<CardRarity> {
    ALL("All"),
    COMMON("Common"),
    RARE("Rare"),
    EPIC("Epic"),
    LEGENDARY("Legendary")
}
