package com.example.exp4.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * CardMatchingGame 游戏模型的单元测试
 */
class CardMatchingGameTest {

    private val GAME_SIZE = 16

    @Test
    fun newGameStartsWithZeroScoreAndAllCardsFaceDown() {
        val game = CardMatchingGame(GAME_SIZE)
        assertEquals(0, game.score)
        for (i in 0 until GAME_SIZE) {
            assertFalse(game.cardAtIndex(i).isChosen)
            assertFalse(game.cardAtIndex(i).isMatched)
        }
    }

    @Test
    fun chooseFirstCardCostsOnePointAndBecomesChosen() {
        val game = CardMatchingGame(GAME_SIZE)
        game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)
        assertTrue(game.cardAtIndex(0).isChosen)
    }

    @Test
    fun chooseSameCardAgainCancelsSelectionWithoutExtraCost() {
        val game = CardMatchingGame(GAME_SIZE)
        game.chooseCardAtIndex(0)
        game.chooseCardAtIndex(0)
        assertFalse(game.cardAtIndex(0).isChosen)
        assertEquals(-1, game.score)
    }

    @Test
    fun mismatchedPairLosesPenaltyPlusCost() {
        val game = CardMatchingGame(GAME_SIZE)
        val (a, b) = findPair(game) { x, y -> x.match(arrayOf(y)) == 0 }
        game.chooseCardAtIndex(a)
        game.chooseCardAtIndex(b)
        // -1(选a) -2(不匹配罚分) -1(选b)
        assertEquals(-4, game.score)
        assertFalse(game.cardAtIndex(a).isChosen)
        assertTrue(game.cardAtIndex(b).isChosen)
    }

    @Test
    fun matchedPairGainsBonusAndBothAreMatched() {
        val game = CardMatchingGame(GAME_SIZE)
        val (a, b) = findPair(game) { x, y -> x.match(arrayOf(y)) > 0 }
        val matchScore = game.cardAtIndex(a).match(arrayOf(game.cardAtIndex(b)))
        game.chooseCardAtIndex(a)
        game.chooseCardAtIndex(b)
        assertEquals(-1 + matchScore * 4 - 1, game.score)
        assertTrue(game.cardAtIndex(a).isMatched)
        assertTrue(game.cardAtIndex(b).isMatched)
    }

    @Test
    fun choosingMatchedCardDoesNotChangeScore() {
        val game = CardMatchingGame(GAME_SIZE)
        val (a, b) = findPair(game) { x, y -> x.match(arrayOf(y)) > 0 }
        game.chooseCardAtIndex(a)
        game.chooseCardAtIndex(b)
        val scoreAfterMatch = game.score
        game.chooseCardAtIndex(a)
        assertEquals(scoreAfterMatch, game.score)
    }

    /** 在（可重复新建的）游戏中寻找满足条件的一对牌的索引 */
    private fun findPair(game: CardMatchingGame, predicate: (Card, Card) -> Boolean): Pair<Int, Int> {
        var current = game
        var attempts = 0
        while (attempts < 50) {
            for (i in 0 until GAME_SIZE) {
                for (j in i + 1 until GAME_SIZE) {
                    if (predicate(current.cardAtIndex(i), current.cardAtIndex(j))) {
                        return Pair(i, j)
                    }
                }
            }
            current = CardMatchingGame(GAME_SIZE)
            attempts++
        }
        throw AssertionError("未能找到满足条件的牌对")
    }
}
