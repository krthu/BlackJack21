package com.example.blackjack21

class Hand(
    val bet: Int
) {
    val cards = mutableListOf<Card>()

    fun addCard(card: Card){
        cards.add(card)
    }

    fun getBetAmount(): Int {
        return bet
    }
}