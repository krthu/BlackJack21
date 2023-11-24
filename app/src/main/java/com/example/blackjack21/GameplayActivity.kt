package com.example.blackjack21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class GameplayActivity : AppCompatActivity(), GameplayFragment.GamePlayListener {
    val players = mutableListOf<BlackJackPlayer>()
    val deck = Deck(7)
    val dealerCards = mutableListOf<Card>()
    val dealerCardsImageViews = mutableListOf<ImageView>()
    var isDealerTurn = false
    lateinit var cardValueDealerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameplay)
        setReferances()
        addPlayer("Nils", 100) // Byt mot info from intent
        players[0].makeBet(10)

        if (savedInstanceState == null) {
            val fragment = BetViewFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_gameplay_container, fragment)
                .commit()
        }

    }

    fun replaceFragment(gameplayFragment: GameplayFragment) {
        deck.shuffle()

        dealInitialCards()
        updateDealerCardImages(dealerCards)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_gameplay_container, gameplayFragment)
            .addToBackStack(null)
            .commit()
    }

    fun setReferances() {
        dealerCardsImageViews.add(findViewById(R.id.first_card_dealer))
        dealerCardsImageViews.add(findViewById(R.id.second_card_dealer))
        dealerCardsImageViews.add(findViewById(R.id.third_card_dealer))
        dealerCardsImageViews.add(findViewById(R.id.fourth_card_dealer))
        dealerCardsImageViews.add(findViewById(R.id.fifth_card_dealer))
        dealerCardsImageViews.add(findViewById(R.id.sixth_card_dealer))
        cardValueDealerTextView = findViewById(R.id.card_value_dealer)


    }

    fun addPlayer(name: String, money: Int) {
        players.add(BlackJackPlayer(name, money))
    }

    fun dealInitialCards() {
        for (i in 1..2) {
            players.forEach { player ->
                player.addCard(0, deck.drawACard())
            }
            dealerCards.add(deck.drawACard())
        }
    }

    fun updateDealerCardImages(cards: List<Card>) {
        cards.forEachIndexed { index, card ->
            val imageName = if (!isDealerTurn && index == 1) "card_back" else getImageId(card)
            val imageId = resources.getIdentifier(imageName, "drawable", packageName)
            dealerCardsImageViews[index].setImageResource(imageId)
        }
        updateDealerCardsValue(cards)
    }


    fun updateDealerCardsValue(cards: List<Card>) {
        var value = 0
        if (!isDealerTurn) {
            value = getBlackJackValue(listOf(cards[0]))
        } else {
            value = getBlackJackValue(cards)
        }

        cardValueDealerTextView.text = value.toString()
    }

    // Den här kan vi nog göra något annat med. Den finns på två ställen nu. Kanske lägga den i Card?
    fun getImageId(card: Card): String {
        val builder = StringBuilder()
        when (card.suit) {
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
        if (card.number < 10) {
            builder.append(0)
        }
        builder.append(card.number)
        return builder.toString()
    }

    fun getPlayerBets() {
        players.forEach { player ->
            player.makeBet(50)
        }
    }

    fun playDealerHand() {
        while (getBlackJackValue(dealerCards) < 17) {
            dealerCards.add(deck.drawACard())
            if (dealerCards.size == dealerCardsImageViews.size) {
                // To protect from dealer from running out of imageViews
                if (getBlackJackValue(dealerCards) < 17) {
                    val card = dealerCards.removeLast()
                    Log.d("!!!", "Card removed to not crash! $card")
                }
            }
            updateDealerCardImages(dealerCards)
        }
    }




    override fun getBlackJackValue(cards: List<Card>): Int{
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

    override fun getActivePlayer(): BlackJackPlayer {
        return players[0]
    }

    override fun getActiveDeck(): Deck {
       return deck
    }

    override fun onHitPress() {
        players[0].hands[0].addCard(deck.drawACard())
        //val cards = players[0].hands[0].cards
    }
}