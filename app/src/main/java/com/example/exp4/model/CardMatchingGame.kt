package com.example.exp4.model

/**
 * 翻牌配对游戏模型（三张牌玩法）：
 * 三张牌点数相同或花色相同即配对成功。
 */
class CardMatchingGame(val cardCount: Int) {
    var score = 0
        private set

    private val cards = mutableListOf<Card>()

    init {
        reset()
    }

    /** 重新洗牌发牌，并清零得分与所有牌的状态 */
    fun reset() {
        score = 0
        cards.clear()
        val deck = Deck()
        repeat(cardCount) {
            deck.drawRandomCard()?.let(cards::add)
        }
    }

    fun cardAtIndex(index: Int): Card {
        return cards[index]
    }

    val MISMATCH_PENALTY = 2
    val MATCH_BONUS = 4
    val COST_TO_CHOOSE = 1

    /** 一次配对需要选中的牌数 */
    val CARDS_PER_MATCH = 3

    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (card.isMatched) {
            return
        }
        if (card.isChosen) {
            // 再次点击已翻开的牌：取消选择，不扣分
            card.isChosen = false
            return
        }

        val chosenCards = cards.filter { it !== card && it.isChosen && !it.isMatched }
        if (chosenCards.size == CARDS_PER_MATCH - 1) {
            // 当前牌加上已翻开的两张牌恰好三张，进行配对判定
            val matchScore = card.match(chosenCards.toTypedArray())
            if (matchScore > 0) {
                score += matchScore * MATCH_BONUS
                chosenCards.forEach { it.isMatched = true }
                card.isMatched = true
            } else {
                score -= MISMATCH_PENALTY
                chosenCards.forEach { it.isChosen = false }
            }
        }

        score -= COST_TO_CHOOSE
        card.isChosen = true
    }
}
