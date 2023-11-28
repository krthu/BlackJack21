package com.example.blackjack21

class BlackJackPlayer(name: String, var money: Int): Player(name) {
    val hands = mutableListOf<Hand>()

//    fun getMoney(): Int {
//        return money
//    }
    fun makeBet(bet: Int): Boolean{
        if (money >= bet){
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