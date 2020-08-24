package cafe.adriel.gwentwallpapers.domain.model.card

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

enum class CardFavorite(override val title: String) : AdapterOption<CardFavorite> {
    ALL("All"),
    YES("Yes"),
    NO("No")
}
