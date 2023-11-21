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






}

