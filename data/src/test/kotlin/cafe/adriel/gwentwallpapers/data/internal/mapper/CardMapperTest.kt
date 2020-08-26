package cafe.adriel.gwentwallpapers.data.internal.mapper

import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardBack
import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardFront
import cafe.adriel.gwentwallpapers.domain.model.card.CardColor.GOLD
import cafe.adriel.gwentwallpapers.domain.model.card.CardFaction.NEUTRAL
import cafe.adriel.gwentwallpapers.domain.model.card.CardRarity.EPIC
import cafe.adriel.gwentwallpapers.domain.model.card.CardSet.BASE
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide.BACK
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide.FRONT
import cafe.adriel.gwentwallpapers.domain.model.card.CardType.UNIT
import cafe.adriel.gwentwallpapers.domain.model.wallpaper.Wallpaper
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class CardMapperTest {

    @Test
    fun `should map card front correctly`() {
        val result = cardFrontInput.toWallpaper()

        expectThat(result) isEqualTo cardFrontOutput
    }

    @Test
    fun `should map card back correctly`() {
        val result = cardBackInput.toWallpaper()

        expectThat(result) isEqualTo cardBackOutput
    }

    private val cardFrontInput = GwentCardFront(
        id = 1,
        artId = -1,
        name = "Sample card",
        faction = NEUTRAL,
        set = BASE,
        type = UNIT,
        rarity = EPIC,
        color = GOLD,
        primaryColor = "#000000",
        artist = "Someone"
    )

    private val cardBackInput = GwentCardBack(
        id = 1,
        primaryColor = "#000000"
    )

    private val cardFrontOutput = Wallpaper(
        id = -1,
        name = "Sample card",
        artist = "Someone",
        color = "#000000",
        side = FRONT
    )

    private val cardBackOutput = Wallpaper(
        id = 1,
        name = null,
        artist = null,
        color = "#000000",
        side = BACK
    )
}
