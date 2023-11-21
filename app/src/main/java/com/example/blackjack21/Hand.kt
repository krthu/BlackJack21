package com.example.blackjack21

class Hand(
    protected val bet: Int
) {
    protected val cards = mutableListOf<Card>()

    fun addCard(card: Card){
        cards.add(card)
    }
}