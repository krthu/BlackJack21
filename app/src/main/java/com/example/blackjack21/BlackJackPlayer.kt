package com.example.blackjack21

class BlackJackPlayer(name: String, private var money: Int): Player(name) {
    val hands = mutableListOf<Hand>()

    fun makeBet(bet: Int): Boolean{
        if (money > bet){
            money -= bet
            hands.add(Hand(bet))
            return true
        }
        return false
    }

    fun clearHands(){
        hands.clear()
    }
    fun addCard(indexOfHand: Int, card: Card){
        hands[indexOfHand].addCard(card)
    }

    fun addMoney(moneyToAdd: Int){
        money += moneyToAdd
    }
}