package com.example.blackjack21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val deck = Deck(1)
        Log.d("!!!", "${deck.decklist[0]}" )
        deck.shuffle()
        Log.d("!!!", "${deck.decklist[0]}" )
        Log.d("!!!", "${deck.drawACard()}" )
        Log.d("!!!", "${deck.decklist[0]}" )
        Log.d("!!!", "${deck.decklist.size}" )
    }
}