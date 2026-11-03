package com.example.exp4.model

class Card(private var suit: String, private var rank: String, var isChosen: Boolean = false, var isMatched: Boolean = false) {
    companion object {
        val rankStrings = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val validSuits = arrayOf("♥", "♦", "♠", "♣")
    }

    override fun toString(): String {
        return suit + rank
    }

    /**
     * 计算本牌与其它牌的匹配得分。
     * 传入 1 张牌时按两张牌规则计分（点数相同 4 分、花色相同 1 分）；
     * 传入 2 张牌时按三张牌规则计分（三张点数相同 8 分、三张花色相同 4 分），其余情况 0 分。
     */
    fun match(otherCards: Array<Card>): Int {
        when (otherCards.size) {
            1 -> {
                val otherCard = otherCards[0]
                return when {
                    otherCard.rank == rank -> 4
                    otherCard.suit == suit -> 1
                    else -> 0
                }
            }
            2 -> {
                // 加上本牌共三张：必须三张点数相同或三张花色相同才算配对成功
                val allCards = listOf(this) + otherCards
                val sameRank = allCards.count { it.rank == rank }
                val sameSuit = allCards.count { it.suit == suit }
                return when {
                    sameRank == 3 -> 8
                    sameSuit == 3 -> 4
                    else -> 0
                }
            }
            else -> return 0
        }
    }
}
