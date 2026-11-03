package com.example.exp4.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exp4.R
import com.example.exp4.databinding.ItemCardBinding
import com.example.exp4.model.Card

/**
 * 翻牌游戏适配器（View 层）：
 * 负责把 [Card] 状态绑定到单张牌的界面上，并通过回调把点击事件交还给 Activity。
 */
class CardAdapter(
    private val onCardClick: (position: Int) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    private var cards: List<Card> = emptyList()

    /** 单张牌的高度，由 Activity 根据屏幕可用空间计算后设置 */
    var cardHeight: Int = 0
        set(value) {
            if (field == value) return
            field = value
            notifyItemRangeChanged(0, itemCount)
        }

    /** 提交新的牌堆并刷新列表（牌数变化时使用整体刷新） */
    fun submitCards(newCards: List<Card>) {
        val sizeChanged = cards.size != newCards.size
        cards = newCards
        if (sizeChanged) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeChanged(0, cards.size)
        }
    }

    override fun getItemCount(): Int = cards.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = CardViewHolder(binding)
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onCardClick(position)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        if (cardHeight > 0) {
            holder.itemView.layoutParams = holder.itemView.layoutParams.apply {
                height = cardHeight
            }
        }
        holder.bind(cards[position])
    }

    /** 单张牌的 ViewHolder：根据牌的三种状态切换配色与文字 */
    class CardViewHolder(
        private val binding: ItemCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(card: Card) {
            val context = binding.root.context
            val cardText = card.toString()
            when {
                card.isMatched -> {
                    binding.cardContainer.setCardBackgroundColor(
                        context.getColor(R.color.card_matched_color)
                    )
                    binding.tvCard.text = cardText
                    binding.tvCard.setTextColor(context.getColor(R.color.matched_card_text))
                }

                card.isChosen -> {
                    binding.cardContainer.setCardBackgroundColor(
                        context.getColor(R.color.card_front_color)
                    )
                    val isRedSuit = cardText.startsWith("♥") || cardText.startsWith("♦")
                    binding.tvCard.setTextColor(
                        context.getColor(
                            if (isRedSuit) R.color.red_card_text else R.color.black_card_text
                        )
                    )
                    binding.tvCard.text = cardText
                }

                else -> {
                    binding.cardContainer.setCardBackgroundColor(
                        context.getColor(R.color.card_back_color)
                    )
                    binding.tvCard.text = context.getString(R.string.card_back_text)
                    binding.tvCard.setTextColor(context.getColor(R.color.card_back_text))
                }
            }
        }
    }
}
