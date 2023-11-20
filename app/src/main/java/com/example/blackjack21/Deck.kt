package com.example.blackjack21

class Deck(numberOfDecks: Int) {
    val decklist = mutableListOf<Card>()
    private val numbers = listOf(2,3,4,5,6,7,8,9,10,11,12,13,14)
    private val suits = listOf("Herts", "Diamonds", "Clubs", "Spades")
    init {
        for (i in 1..numberOfDecks){
            suits.forEach{ suit ->
                numbers.forEach{ number ->
                    decklist.add(Card(suit, number))
                }
            }
        }
    }
}