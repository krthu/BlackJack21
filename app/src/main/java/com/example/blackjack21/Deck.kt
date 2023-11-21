package com.example.blackjack21

class Deck(private var numberOfDecks: Int) {
    val decklist = mutableListOf<Card>()
    private val numbers = listOf(2,3,4,5,6,7,8,9,10,11,12,13,14)
    private val suits = listOf("Hearts", "Diamonds", "Clubs", "Spades")
    init {
        createDeckList()
    }

    fun shuffle(){
        decklist.shuffle()
    }

    fun drawACard(): Card{
        // Perhaps change this to shuffle in new decks if this is happens so we can return a card? Discuss
        if (decklist.isEmpty()){
            createDeckList()
            decklist.shuffle()
        }
        return decklist.removeAt(0)
    }

    fun createDeckList(){
        for (i in 1..numberOfDecks){
            suits.forEach{ suit ->
                numbers.forEach{ number ->
                    decklist.add(Card(suit, number))
                }
            }
        }
    }


}