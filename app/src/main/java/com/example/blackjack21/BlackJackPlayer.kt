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

    fun doubleBetInHand(handIndex: Int): Boolean{
        if (money > hands[handIndex].bet)
        {
            money -= hands[handIndex].bet
            hands[handIndex].doubleBet()
            return true
        }
        return false
    }


    fun addCard(indexOfHand: Int, card: Card) {
        hands[indexOfHand].addCard(card)
    }

    fun split(): Boolean{
        if (makeBet(hands[0].bet)){
            val cardToMove = hands[0].cards.removeAt(1)
            hands[1].cards.add(cardToMove)
            return true
        }
        return false
    }

    fun ableToSplit():Boolean{
        if(hands[0].cards[0].number == hands[0].cards[1].number){
            return true
        }
        return false
    }
}
