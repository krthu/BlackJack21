package com.example.blackjack21

class Hand(
    var bet: Double
) {
    val cards = mutableListOf<Card>()

    fun addCard(card: Card){
        cards.add(card)
    }

    fun getBetAmount(): Double {
        return bet
    }

    fun doubleBet(){
        bet *= 2
    }
}