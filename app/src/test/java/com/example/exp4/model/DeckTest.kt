package com.example.exp4.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Deck 牌的单元测试
 */
class DeckTest {

    @Test
    fun drawAll52CardsReturnsUniqueCards() {
        val deck = Deck()
        val drawn = mutableListOf<String>()
        repeat(52) {
            val card = deck.drawRandomCard()
            assertNotNull(card)
            drawn.add(card!!.toString())
        }
        // 52 张牌互不重复
        assertEquals(52, drawn.distinct().size)
    }

    @Test
    fun drawFromEmptyDeckReturnsNull() {
        val deck = Deck()
        repeat(52) { deck.drawRandomCard() }
        assertNull(deck.drawRandomCard())
    }

    @Test
    fun drawnCardsComeFromValidDeck() {
        val deck = Deck()
        repeat(20) {
            val card = deck.drawRandomCard()!!
            assertTrue(Card.validSuits.any { suit -> card.toString().startsWith(suit) })
        }
    }
}
