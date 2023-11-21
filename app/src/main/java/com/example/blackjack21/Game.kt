package com.example.blackjack21

class Game {
    val players = mutableListOf<BlackJackPlayer>() // Ska det vara player?
    val deck = Deck(7)
    val dealerCards = mutableListOf<Card>()

    fun addPlayer(name: String, money: Int){
        players.add(BlackJackPlayer(name, money))
    }

    fun dealInitialCards(){
        for (i in 1..2){
            players.forEach { player ->
                player.addCard(0, deck.drawACard())
            }
            dealerCards.add(deck.drawACard())
        }
    }

    fun getPlayerBets(){
        players.forEach { player ->
            player.makeBet(50)
         }
    }

    fun getBlackJackValue(cards: List<Card>): Int{
        var value = 0
        var numberOfAces = 0
        cards.forEach{card ->
            when (card.number){
                2,3,4,5,6,7,8,9, -> {
                    value += card.number
                }
                10,11,12,13 -> {
                    value += 10
                }
                14 -> {
                    value += 11
                    numberOfAces++
                }
            }
            while (value > 21 && numberOfAces > 0){
                value -= 10
                numberOfAces--
            }
        }
        return value
    }






}

