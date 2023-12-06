package com.example.blackjack21

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class PlayerHandView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val cardImageViews = mutableListOf<ImageView>()
    var betTextView: TextView
    var cardValuePlayerTextView: TextView
    init {
        LayoutInflater.from(context).inflate(R.layout.player_hand, this, true)
        cardImageViews.add(findViewById(R.id.first_card_player))
        cardImageViews.add(findViewById(R.id.second_card_player))
        cardImageViews.add(findViewById(R.id.third_card_player))
        cardImageViews.add(findViewById(R.id.fourth_card_player))
        cardImageViews.add(findViewById(R.id.fifth_card_player))
        betTextView = findViewById(R.id.bet_amount_player)
        cardValuePlayerTextView = findViewById(R.id.card_value_player)

    }

    // To add card to same index imageView can never over 5 because of 5 card charlie rule
    fun setImage(cards: List<Card>) {
         cards.forEachIndexed{index,  card ->
            val imageId = resources.getIdentifier(card.imageString, "drawable", context.packageName)
            cardImageViews[index].setImageResource(imageId)
         }
    }

    fun setBetText(text: String) {
        betTextView.text = "$${text}"
    }

    fun setValueText(value: Int){
        cardValuePlayerTextView.text = value.toString()
    }

    fun startResultAnimation(change: Double){
        var sign = "+"
        var amount = 0
        if (change < 0){
            sign = "-"
            amount = (change     * -1).toInt()
            betTextView.text = ("${sign}$${amount.toString()}")
            betTextView.setTextColor(Color.RED)
            betTextView.setShadowLayer(2f, 1f, 1f, Color.WHITE)
            Animations.looseAnimation(betTextView)

        }else{
            amount = change.toInt()
            betTextView.text = ("${sign}$${amount.toString()}")
            betTextView.setTextColor(Color.GREEN)
            betTextView.setShadowLayer(2f, 1f, 1f, Color.WHITE)
            Animations.winAnimation(betTextView)
        }
    }
}