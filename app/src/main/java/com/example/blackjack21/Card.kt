package com.example.blackjack21

class Card(protected val suit: String, protected val number: Int) {

    override fun toString(): String{
        return ("${number} of ${suit}")
    }
}