package com.example.exp4

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.exp4.databinding.ActivityCardGameBinding
import com.example.exp4.model.CardMatchingGame

/**
 * 翻牌游戏 Activity（Controller）：
 * 接受用户点击交互 -> 处理 CardMatchingGame 模型 -> 同步刷新牌面界面（View）
 */
class CardGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardGameBinding
    private lateinit var game: CardMatchingGame
    private lateinit var cardViews: List<TextView>

    private val cardIds = intArrayOf(
        R.id.card0, R.id.card1, R.id.card2, R.id.card3,
        R.id.card4, R.id.card5, R.id.card6, R.id.card7,
        R.id.card8, R.id.card9, R.id.card10, R.id.card11,
        R.id.card12, R.id.card13, R.id.card14, R.id.card15
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.title_card_game)

        cardViews = cardIds.map { findViewById<TextView>(it) }
        cardViews.forEachIndexed { index, view ->
            view.setOnClickListener { onCardClicked(index) }
        }

        binding.btnReset.setOnClickListener { startNewGame() }
        binding.btnExit.setOnClickListener { finish() }

        startNewGame()
    }

    /** 开始新的一局游戏 */
    private fun startNewGame() {
        game = CardMatchingGame(cardViews.size)
        updateUI()
    }

    /** 用户点击某张牌：交给模型处理并同步界面 */
    private fun onCardClicked(index: Int) {
        game.chooseCardAtIndex(index)
        PlayerStore.submitScore(game.score)
        updateUI()
    }

    /** 根据模型状态刷新所有牌的显示和得分 */
    private fun updateUI() {
        for (i in cardViews.indices) {
            val card = game.cardAtIndex(i)
            val view = cardViews[i]
            when {
                card.isMatched -> {
                    view.setBackgroundResource(R.drawable.card_matched)
                    view.text = card.toString()
                    view.setTextColor(getColor(R.color.matched_card_text))
                }
                card.isChosen -> {
                    view.setBackgroundResource(R.drawable.card_front)
                    view.text = card.toString()
                    view.setTextColor(getCardColor(card.toString()))
                }
                else -> {
                    view.setBackgroundResource(R.drawable.card_back)
                    view.text = getString(R.string.card_back_text)
                    view.setTextColor(getColor(R.color.card_back_text))
                }
            }
        }
        binding.tvScore.text = game.score.toString()
    }

    /** 红桃/方块显示红色，黑桃/梅花显示黑色 */
    private fun getCardColor(cardText: String): Int {
        return if (cardText.startsWith("♥") || cardText.startsWith("♦")) {
            getColor(R.color.red_card_text)
        } else {
            getColor(R.color.black_card_text)
        }
    }
}
