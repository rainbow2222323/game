package com.example.exp4

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.exp4.adapter.CardAdapter
import com.example.exp4.databinding.ActivityCardGameBinding
import com.example.exp4.model.CardMatchingGame

/**
 * 翻牌游戏 Activity（Controller）：
 * 接收 RecyclerView 的回调 -> 交给 CardMatchingGame 模型处理 -> 通知 CardAdapter 刷新牌面。
 * 竖屏显示 24 张牌（4 列），横屏显示 16 张牌（8 列），牌数配置见 values/integers.xml。
 */
class CardGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardGameBinding
    private lateinit var adapter: CardAdapter
    private lateinit var game: CardMatchingGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.title_card_game)

        // 适配 edge-to-edge：避免牌面被系统导航栏遮挡
        val gamePadding = resources.getDimensionPixelSize(R.dimen.game_padding)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                gamePadding + systemBars.left,
                gamePadding,
                gamePadding + systemBars.right,
                gamePadding + systemBars.bottom
            )
            insets
        }

        game = CardMatchingGame(cardCount())

        adapter = CardAdapter { position -> onCardClicked(position) }
        binding.rvCards.layoutManager = GridLayoutManager(this, spanCount())
        binding.rvCards.adapter = adapter
        // 翻牌状态需要立即刷新，关闭默认的淡入淡出动画
        binding.rvCards.itemAnimator = null
        binding.rvCards.addOnLayoutChangeListener { _, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (right - left != oldRight - oldLeft || bottom - top != oldBottom - oldTop) {
                binding.rvCards.post { updateCardHeight() }
            }
        }

        binding.btnReset.setOnClickListener { resetGame() }
        binding.btnExit.setOnClickListener { finish() }

        adapter.submitCards(currentCards())
        binding.tvScore.text = game.score.toString()
    }

    /** 当前方向下应显示的牌数（竖屏 24、横屏 16） */
    private fun cardCount(): Int = resources.getInteger(R.integer.card_count)

    /** 当前方向下网格的列数（竖屏 4、横屏 8） */
    private fun spanCount(): Int = resources.getInteger(R.integer.card_grid_span)

    /** 把模型中的牌导出为适配器需要的列表 */
    private fun currentCards() = List(game.cardCount) { game.cardAtIndex(it) }

    /** 重置按钮：调用模型的 reset 方法重新发牌 */
    private fun resetGame() {
        game.reset()
        adapter.submitCards(currentCards())
        binding.tvScore.text = game.score.toString()
    }

    /** 用户点击某张牌：交给模型处理，再刷新牌面和得分 */
    private fun onCardClicked(position: Int) {
        game.chooseCardAtIndex(position)
        adapter.submitCards(currentCards())
        PlayerStore.submitScore(game.score)
        binding.tvScore.text = game.score.toString()
    }

    /**
     * 根据 RecyclerView 的可用高度与行数计算单张牌高度，
     * 让牌面铺满屏幕且横竖屏都保持规整。
     */
    private fun updateCardHeight() {
        val span = spanCount()
        if (span <= 0 || game.cardCount == 0) return
        val rows = (game.cardCount + span - 1) / span
        val margin = resources.getDimensionPixelSize(R.dimen.card_margin)
        val available =
            binding.rvCards.height - binding.rvCards.paddingTop - binding.rvCards.paddingBottom
        if (rows == 0 || available <= 0) return
        val height = available / rows - margin * 2
        adapter.cardHeight =
            height.coerceAtLeast(resources.getDimensionPixelSize(R.dimen.card_min_height))
    }

    /** 旋转屏幕时切换列数与牌数；牌数变化则重新发牌 */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        (binding.rvCards.layoutManager as? GridLayoutManager)?.spanCount = spanCount()

        val newCardCount = cardCount()
        if (newCardCount != game.cardCount) {
            game = CardMatchingGame(newCardCount)
            adapter.submitCards(currentCards())
            binding.tvScore.text = game.score.toString()
            Toast.makeText(this, R.string.toast_orientation_changed, Toast.LENGTH_SHORT).show()
        }
        binding.rvCards.post { updateCardHeight() }
    }
}
