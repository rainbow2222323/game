package com.example.exp4.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * CardMatchingGame 游戏模型的单元测试（三张牌玩法）
 */
class CardMatchingGameTest {

    private val GAME_SIZE = 24

    @Test
    fun newGameStartsWithZeroScoreAndAllCardsFaceDown() {
        val game = CardMatchingGame(GAME_SIZE)
        assertEquals(GAME_SIZE, game.cardCount)
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
    fun twoChosenCardsWaitForTheThirdCard() {
        val game = CardMatchingGame(GAME_SIZE)
        game.chooseCardAtIndex(0)
        game.chooseCardAtIndex(1)
        // 只选中两张牌时不做配对判定：-1(选第 1 张) -1(选第 2 张)
        assertEquals(-2, game.score)
        assertTrue(game.cardAtIndex(0).isChosen)
        assertTrue(game.cardAtIndex(1).isChosen)
        assertFalse(game.cardAtIndex(0).isMatched)
        assertFalse(game.cardAtIndex(1).isMatched)
    }

    @Test
    fun mismatchedTripleLosesPenaltyAndFlipsPreviousCards() {
        val game = CardMatchingGame(GAME_SIZE)
        val (a, b, c) = findTriple(game) { x, y, z -> x.match(arrayOf(y, z)) == 0 }
        game.chooseCardAtIndex(a)
        game.chooseCardAtIndex(b)
        game.chooseCardAtIndex(c)
        // -1(选 a) -1(选 b) -2(不匹配罚分) -1(选 c)
        assertEquals(-5, game.score)
        assertFalse(game.cardAtIndex(a).isChosen)
        assertFalse(game.cardAtIndex(b).isChosen)
        assertTrue(game.cardAtIndex(c).isChosen)
        assertFalse(game.cardAtIndex(a).isMatched)
        assertFalse(game.cardAtIndex(b).isMatched)
        assertFalse(game.cardAtIndex(c).isMatched)
    }

    @Test
    fun matchedTripleGainsBonusAndAllCardsMatched() {
        val game = CardMatchingGame(GAME_SIZE)
        val (a, b, c) = findTriple(game) { x, y, z -> x.match(arrayOf(y, z)) > 0 }
        val matchScore = game.cardAtIndex(a)
            .match(arrayOf(game.cardAtIndex(b), game.cardAtIndex(c)))
        game.chooseCardAtIndex(a)
        game.chooseCardAtIndex(b)
        game.chooseCardAtIndex(c)
        // -1(选 a) -1(选 b) + 奖励分 -1(选 c)
        assertEquals(-3 + matchScore * 4, game.score)
        assertTrue(game.cardAtIndex(a).isMatched)
        assertTrue(game.cardAtIndex(b).isMatched)
        assertTrue(game.cardAtIndex(c).isMatched)
    }

    @Test
    fun choosingMatchedCardDoesNotChangeScore() {
        val game = CardMatchingGame(GAME_SIZE)
        val (a, b, c) = findTriple(game) { x, y, z -> x.match(arrayOf(y, z)) > 0 }
        game.chooseCardAtIndex(a)
        game.chooseCardAtIndex(b)
        game.chooseCardAtIndex(c)
        val scoreAfterMatch = game.score
        game.chooseCardAtIndex(a)
        assertEquals(scoreAfterMatch, game.score)
    }

    @Test
    fun resetClearsScoreAndAllCardStates() {
        val game = CardMatchingGame(GAME_SIZE)
        game.chooseCardAtIndex(0)
        game.reset()
        assertEquals(0, game.score)
        assertEquals(GAME_SIZE, game.cardCount)
        for (i in 0 until GAME_SIZE) {
            assertFalse(game.cardAtIndex(i).isChosen)
            assertFalse(game.cardAtIndex(i).isMatched)
        }
    }

    /** 在（可重复新建的）游戏中寻找满足条件的三张牌的索引 */
    private fun findTriple(
        game: CardMatchingGame,
        predicate: (Card, Card, Card) -> Boolean
    ): Triple<Int, Int, Int> {
        var current = game
        var attempts = 0
        while (attempts < 50) {
            for (i in 0 until current.cardCount) {
                for (j in i + 1 until current.cardCount) {
                    for (k in j + 1 until current.cardCount) {
                        if (predicate(
                                current.cardAtIndex(i),
                                current.cardAtIndex(j),
                                current.cardAtIndex(k)
                            )
                        ) {
                            return Triple(i, j, k)
                        }
                    }
                }
            }
            current = CardMatchingGame(current.cardCount)
            attempts++
        }
        throw AssertionError("未能找到满足条件的三张牌")
    }
}
