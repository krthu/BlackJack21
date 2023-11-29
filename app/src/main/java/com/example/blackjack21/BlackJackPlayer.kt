package com.example.blackjack21

class BlackJackPlayer(name: String, var money: Double) : Player(name) {
    val hands = mutableListOf<Hand>()

    // Förenklad metod för att uppdatera saldot
    fun updateBalance(amount: Double) {
        money += amount
    }

    fun addMoney(amount: Double) {
        money += amount
    }

    fun makeBet(bet: Double): Boolean {
        if (bet <= money) {
            updateBalance(-bet)  // Dra av satsningen från saldot
            hands.add(Hand(bet))
            return true
        }
        return false
    }

    fun clearHands() {
        hands.clear()
    }

    fun addCard(indexOfHand: Int, card: Card) {
        hands[indexOfHand].addCard(card)
    }
}
