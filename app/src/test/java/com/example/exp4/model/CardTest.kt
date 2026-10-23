package com.example.exp4.model

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Card 模型的单元测试
 */
class CardTest {

    @Test
    fun toStringReturnsSuitPlusRank() {
        assertEquals("♥A", Card(suit = "♥", rank = "A").toString())
        assertEquals("♠10", Card(suit = "♠", rank = "10").toString())
    }

    @Test
    fun matchSameRankScores4() {
        val card = Card(suit = "♥", rank = "5")
        val other = Card(suit = "♠", rank = "5")
        assertEquals(4, card.match(arrayOf(other)))
    }

    @Test
    fun matchSameSuitScores1() {
        val card = Card(suit = "♥", rank = "3")
        val other = Card(suit = "♥", rank = "9")
        assertEquals(1, card.match(arrayOf(other)))
    }

    @Test
    fun matchDifferentRankAndSuitScores0() {
        val card = Card(suit = "♥", rank = "3")
        val other = Card(suit = "♠", rank = "9")
        assertEquals(0, card.match(arrayOf(other)))
    }

    @Test
    fun matchWithEmptyCardsScores0() {
        val card = Card(suit = "♥", rank = "3")
        assertEquals(0, card.match(arrayOf()))
    }

    @Test
    fun newCardIsNotChosenNorMatched() {
        val card = Card(suit = "♦", rank = "7")
        assertEquals(false, card.isChosen)
        assertEquals(false, card.isMatched)
    }

    @Test
    fun companionExposes13RanksAnd4Suits() {
        assertEquals(13, Card.rankStrings.size)
        assertEquals(4, Card.validSuits.size)
        assertEquals(52, Card.rankStrings.size * Card.validSuits.size)
    }
}
