package com.example.blackjack21


class Deck(private var numberOfDecks: Int) {
    private val decklist = mutableListOf<Card>()
    private val numbers = listOf(2,3,4,5,6,7,8,9,10,11,12,13,14)
    private val suits = listOf("Hearts", "Diamonds", "Clubs", "Spades")
    init {
        createDeckList()
    }

    fun shuffle(){
        decklist.shuffle()
    }

    fun drawACard(): Card{
        if (decklist.isEmpty()){
            createDeckList()
            decklist.shuffle()
        }
        return decklist.removeAt(0)
    }

    private fun createDeckList(){
        for (i in 1..numberOfDecks){
            suits.forEach{ suit ->
                numbers.forEach{ number ->
                    val imageString = getImageId(suit, number)

                    decklist.add(Card(suit, number, imageString))
                }
            }
        }
    }

    fun stackDeck (){
//        decklist.add(0,Card("Heart", 14, "h14"))
//        decklist.add(1,Card("Heart", 13, "h13"))
//        decklist.add(2,Card("Heart", 13, "h13"))
//        decklist.add(3,Card("Heart", 13, "h13"))
//        decklist.add(4,Card("Heart", 14, "h14"))

        decklist.add(0,Card("Heart", 2, "h02"))
        decklist.add(0,Card("Heart", 2, "h02"))
        decklist.add(0,Card("Heart", 2, "h02"))
        decklist.add(0,Card("Heart", 2, "h02"))
        decklist.add(0,Card("Heart", 2, "d02"))
        decklist.add(0,Card("Heart", 2, "c02"))
        decklist.add(0,Card("Heart", 2, "s02"))
        decklist.add(0,Card("Heart", 2, "h03"))
        decklist.add(0,Card("Heart", 2, "c03"))
        decklist.add(0,Card("Heart", 2, "s03"))
    }


    private fun getImageId(suit: String, number: Int): String     {
        val builder = StringBuilder()
        when (suit) {
            "Hearts" -> {
                builder.append("h")
            }

            "Diamonds" -> {
                builder.append("d")
            }

            "Clubs" -> {
                builder.append("c")
            }

            "Spades" -> {
                builder.append("s")
            }
        }
        if (number < 10) {
            builder.append(0)
        }
        builder.append(number)
        return builder.toString()
    }

}