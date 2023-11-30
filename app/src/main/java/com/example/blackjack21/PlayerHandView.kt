package com.example.blackjack21

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

class PlayerHandView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val cardImageViews = mutableListOf<ImageView>()
    var currentImageIndex = 0
    init {
        LayoutInflater.from(context).inflate(R.layout.player_hand, this, true)
        cardImageViews.add(findViewById(R.id.first_card_player))
        cardImageViews.add(findViewById(R.id.second_card_player))
        cardImageViews.add(findViewById(R.id.third_card_player))
        cardImageViews.add(findViewById(R.id.fourth_card_player))
        cardImageViews.add(findViewById(R.id.first_card_player))

    }

    // Funktioner för att sätta dynamiskt innehåll
    fun setImage(card: Card) {
        val imageId = resources.getIdentifier(card.imageString, "drawable", context.packageName)
        cardImageViews[currentImageIndex].setImageResource(imageId)
        currentImageIndex++
    }

    fun setTexts(texts: List<String>) {
        // Implementera logiken för att sätta texter
    }
}