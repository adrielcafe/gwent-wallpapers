package cafe.adriel.gwentwallpapers.domain.model.card

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

enum class CardSet(override val title: String) : AdapterOption<CardSet> {
    ALL("All"),
    STARTER("Starter"),
    BASE("Base"),
    THRONEBREAKER("Thronebreaker"),
    CRIMSON_CURSE("Crimson Curse"),
    NOVIGRAD("Novigrad"),
    IRON_JUDGEMENT("Iron Judgement"),
    MERCHANTS_OF_OFIR("Merchants of Ofir"),
    MASTER_MIRROR("Master Mirror")
}
