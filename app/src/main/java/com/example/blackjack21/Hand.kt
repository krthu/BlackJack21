package com.example.blackjack21

class Hand(
    val bet: Double
) {
    val cards = mutableListOf<Card>()

    fun addCard(card: Card){
        cards.add(card)
    }

    fun getBetAmount(): Double {
        return bet
    }
}