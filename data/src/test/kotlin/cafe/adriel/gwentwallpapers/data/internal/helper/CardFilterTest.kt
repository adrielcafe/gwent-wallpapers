package cafe.adriel.gwentwallpapers.data.internal.helper

import cafe.adriel.gwentwallpapers.data.internal.ktx.filterCardBacks
import cafe.adriel.gwentwallpapers.data.internal.ktx.filterCardFronts
import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardBack
import cafe.adriel.gwentwallpapers.data.internal.model.GwentCardFront
import cafe.adriel.gwentwallpapers.domain.model.card.CardColor
import cafe.adriel.gwentwallpapers.domain.model.card.CardFaction
import cafe.adriel.gwentwallpapers.domain.model.card.CardFavorite
import cafe.adriel.gwentwallpapers.domain.model.card.CardRarity
import cafe.adriel.gwentwallpapers.domain.model.card.CardSet
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide
import cafe.adriel.gwentwallpapers.domain.model.card.CardType
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import java.util.stream.Stream
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEmpty

class CardFilterTest {

    @Nested
    inner class FilterCardFronts {

        @Test
        fun `when filter by unknown name then return empty`() {
            val result = allCards.filterCardFronts(SearchFilter(name = "xyz"), favorites)

            expectThat(result.toList()).isEmpty()
        }

        @Test
        fun `when filter by favorite is true then return only favorites`() {
            val result = allCards.filterCardFronts(SearchFilter(favorite = CardFavorite.YES), favorites)

            expectThat(result.toList()) containsExactly listOf(card1, card3)
        }

        @Test
        fun `when filter by favorite is false then return all but favorites`() {
            val result = allCards.filterCardFronts(SearchFilter(favorite = CardFavorite.NO), favorites)

            expectThat(result.toList()) containsExactly listOf(card2, card4, card5)
        }

        @Test
        fun `when filter by side back then return empty`() {
            val result = allCards.filterCardFronts(SearchFilter(side = CardSide.BACK), favorites)

            expectThat(result.toList()).isEmpty()
        }

        @TestFactory
        fun `should return all when`(): Stream<DynamicTest> =
            Stream.of(
                "name is blank" to SearchFilter(name = ""),
                "favorite is all" to SearchFilter(favorite = CardFavorite.ALL),
                "side is all" to SearchFilter(side = CardSide.ALL),
                "side is front" to SearchFilter(side = CardSide.FRONT),
                "faction is all" to SearchFilter(faction = CardFaction.ALL),
                "set is all" to SearchFilter(set = CardSet.ALL),
                "type is all" to SearchFilter(type = CardType.ALL),
                "rarity is all" to SearchFilter(rarity = CardRarity.ALL),
                "color is all" to SearchFilter(color = CardColor.ALL)
            ).map { (filterName, filter) ->
                dynamicTest(filterName) {
                    val result = allCards.filterCardFronts(filter, favorites)

                    expectThat(result.toList()) containsExactly allCards.toList()
                }
            }

        @TestFactory
        fun `should return correct when`(): Stream<DynamicTest> =
            Stream.of(
                Triple("name matches", SearchFilter(name = "SEt"), card2),
                Triple("faction matches", SearchFilter(faction = CardFaction.SKELLIGE), card1),
                Triple("set matches", SearchFilter(set = CardSet.MASTER_MIRROR), card2),
                Triple("type matches", SearchFilter(type = CardType.SPECIAL), card3),
                Triple("rarity matches", SearchFilter(rarity = CardRarity.EPIC), card4),
                Triple("color matches", SearchFilter(color = CardColor.GOLD), card5)
            ).map { (filterName, filter, expected) ->
                dynamicTest(filterName) {
                    val result = allCards.filterCardFronts(filter, favorites)

                    expectThat(result.toList()) containsExactly listOf(expected)
                }
            }

        private val favorites = setOf(-1, -3)

        private val sampleCard = GwentCardFront(
            id = 0,
            artId = 1,
            name = "Card",
            faction = CardFaction.NEUTRAL,
            set = CardSet.STARTER,
            type = CardType.UNIT,
            rarity = CardRarity.COMMON,
            color = CardColor.BRONZE,
            primaryColor = "#000000",
            artist = "Someone"
        )
        private val card1 = sampleCard.copy(id = 1, artId = -1, name = "Faction", faction = CardFaction.SKELLIGE)
        private val card2 = sampleCard.copy(id = 2, artId = -2, name = "Set", set = CardSet.MASTER_MIRROR)
        private val card3 = sampleCard.copy(id = 3, artId = -3, name = "Type", type = CardType.SPECIAL)
        private val card4 = sampleCard.copy(id = 4, artId = -4, name = "Rarity", rarity = CardRarity.EPIC)
        private val card5 = sampleCard.copy(id = 5, artId = -5, name = "Color", color = CardColor.GOLD)
        private val allCards = sequenceOf(card1, card2, card3, card4, card5)
    }

    @Nested
    inner class FilterCardBacks {

        @Test
        fun `when filter by favorite is true then return only favorites`() {
            val result = allCards.filterCardBacks(SearchFilter(favorite = CardFavorite.YES), favorites)

            expectThat(result.toList()) containsExactly listOf(card1, card3)
        }

        @Test
        fun `when filter by favorite is false then return all but favorites`() {
            val result = allCards.filterCardBacks(SearchFilter(favorite = CardFavorite.NO), favorites)

            expectThat(result.toList()) containsExactly listOf(card2, card4, card5)
        }

        @TestFactory
        fun `should return all when`(): Stream<DynamicTest> =
            Stream.of(
                "name is blank" to SearchFilter(name = ""),
                "favorite is all" to SearchFilter(favorite = CardFavorite.ALL),
                "side is all" to SearchFilter(side = CardSide.ALL),
                "side is back" to SearchFilter(side = CardSide.BACK),
                "faction is all" to SearchFilter(faction = CardFaction.ALL),
                "set is all" to SearchFilter(set = CardSet.ALL),
                "type is all" to SearchFilter(type = CardType.ALL),
                "rarity is all" to SearchFilter(rarity = CardRarity.ALL),
                "color is all" to SearchFilter(color = CardColor.ALL)
            ).map { (filterName, filter) ->
                dynamicTest(filterName) {
                    val result = allCards.filterCardBacks(filter, favorites)

                    expectThat(result.toList()) containsExactly allCards.toList()
                }
            }

        @TestFactory
        fun `should return empty when`(): Stream<DynamicTest> =
            Stream.of(
                "name is not blank" to SearchFilter(name = "card"),
                "side is front" to SearchFilter(side = CardSide.FRONT),
                "faction is any" to SearchFilter(faction = CardFaction.SKELLIGE),
                "set is any" to SearchFilter(set = CardSet.STARTER),
                "type is any" to SearchFilter(type = CardType.UNIT),
                "rarity is any" to SearchFilter(rarity = CardRarity.COMMON),
                "color is any" to SearchFilter(color = CardColor.GOLD)
            ).map { (filterName, filter) ->
                dynamicTest(filterName) {
                    val result = allCards.filterCardBacks(filter, favorites)

                    expectThat(result.toList()).isEmpty()
                }
            }

        private val favorites = setOf(1, 3)

        private val sampleCard = GwentCardBack(
            id = 0,
            primaryColor = "#FFFFFF"
        )
        private val card1 = sampleCard.copy(id = 1)
        private val card2 = sampleCard.copy(id = 2)
        private val card3 = sampleCard.copy(id = 3)
        private val card4 = sampleCard.copy(id = 4)
        private val card5 = sampleCard.copy(id = 5)
        private val allCards = sequenceOf(card1, card2, card3, card4, card5)
    }
}
