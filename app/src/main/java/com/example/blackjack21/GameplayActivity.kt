package com.example.blackjack21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast

class GameplayActivity : AppCompatActivity(), GameplayFragment.GamePlayListener {

    lateinit var cardValueDealerTextView: TextView
    lateinit var cogWheelMenu: ImageView
    val players = mutableListOf<BlackJackPlayer>()
    val deck = Deck(7)
    val dealerCards = mutableListOf<Card>()
    val dealerCardsImageViews = mutableListOf<ImageView>()
    var isDealerTurn = false
    var gameIsActive = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameplay)
        setReferances()
        addPlayer("Nils", 1000) // Byt mot info from intent
        players[0].makeBet(10)
        deck.shuffle()

        if (savedInstanceState == null) {
            val fragment = BetViewFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_gameplay_container, fragment)
                .commit()
        }

        cogWheelMenu = findViewById(R.id.cogwheel_gameplay)
        cogWheelMenu.setOnClickListener{ view ->
            showPopUpMenu(view)
        }

    }

    override fun getBlackJackValue(cards: List<Card>): Int {
        var value = 0
        var numberOfAces = 0
        cards.forEach { card ->
            when (card.number) {
                2, 3, 4, 5, 6, 7, 8, 9 -> {
                    value += card.number
                }

                10, 11, 12, 13 -> {
                    value += 10
                }

                14 -> {
                    value += 11
                    numberOfAces++
                }
            }
            while (value > 21 && numberOfAces > 0) {
                value -= 10
                numberOfAces--
            }
        }
        return value
    }

    override fun updatePlayerCards() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment
        fragment?.updatePlayerCards(players[0].hands[0].cards)
        fragment?.updatePlayerCardValue(getBlackJackValue(players[0].hands[0].cards))
    }

    override fun onHitPress() {
        players[0].hands[0].addCard(deck.drawACard())
        updatePlayerCards()

        if (getBlackJackValue(players[0].hands[0].cards) > 21) {
            checkWinner()
        }
    }

    override fun onStandPress() {
        playDealerHand()

        checkWinner()
    }

    private fun dealInitialCards() {
        val handler = Handler(Looper.getMainLooper())
        val delayBetweenCards = 500L

        for (i in 0 until 2) {
            handler.postDelayed({
                val card = deck.drawACard()
                players[0].addCard(0, card)

                val fragment =
                    supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment
                fragment?.updatePlayerCards(players[0].hands[0].cards)
                fragment?.updatePlayerCardValue(getBlackJackValue(players[0].hands[0].cards))

                // Check for blackjack after second card
                if (i == 1 && getBlackJackValue(players[0].hands[0].cards) == 21) {
                    checkWinner()
                }
            }, delayBetweenCards * (2 * i))



            handler.postDelayed({
                val dealerCard = deck.drawACard()
                dealerCards.add(dealerCard)

                if (i == 1) {
                    dealerCardsImageViews.getOrNull(i)?.setImageResource(R.drawable.card_back)
                } else {
                    updateDealerCardImages(dealerCards)
                }
            }, delayBetweenCards * (2 * i + 1))
        }


    }
    private fun showPopUpMenu(view: View) {
        val popup = PopupMenu(this, view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.gameplay_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_quit -> {
                    finish()
                    true
                }
                else -> false
            }
        }
        popup.show()
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

    fun updateDealerCardImages(cards: List<Card>) {
        if (cards.isNotEmpty()) {
            cards.forEachIndexed { index, card ->
                val imageName = if (!isDealerTurn && index == 1) "card_back" else getImageId(card)
                val imageId = resources.getIdentifier(imageName, "drawable", packageName)
                dealerCardsImageViews.getOrNull(index)?.setImageResource(imageId)
            }
        }
    }

    fun updateDealerCardsValue(cards: List<Card>) {
        var value = 0
        if (cards.isNotEmpty()) {
            if (!isDealerTurn) {
                value = getBlackJackValue(listOf(cards[0]))
            } else {
                value = getBlackJackValue(cards)
            }
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
        isDealerTurn = true
        updateDealerCardImages(dealerCards)
        updateDealerCardsValue(dealerCards)

        val handler = Handler(Looper.getMainLooper())
        val delayBetweenDealerCards = 500L

        fun drawDealerCard() {
            if (gameIsActive && getBlackJackValue(dealerCards) < 17) {
                dealerCards.add(deck.drawACard())
                updateDealerCardImages(dealerCards)
                updateDealerCardsValue(dealerCards)
                handler.postDelayed(::drawDealerCard, delayBetweenDealerCards)
            } else {
                handler.postDelayed({
                    checkWinner()
                }, 2000)
            }
        }

        handler.postDelayed(::drawDealerCard, delayBetweenDealerCards)
    }

    fun checkWinner() {
        val playerValue = getBlackJackValue(players[0].hands[0].cards)
        val dealerValue = getBlackJackValue(dealerCards)

        when {
            playerValue > 21 -> {
                Toast.makeText(this, "Player Busts!", Toast.LENGTH_SHORT).show()
            }
            dealerValue > 21 -> {
                Toast.makeText(this, "Dealer Busts, Player Wins!", Toast.LENGTH_SHORT).show()
            }
            playerValue == dealerValue -> {
                Toast.makeText(this, "Its a draw!", Toast.LENGTH_SHORT).show()
            }
            playerValue > dealerValue -> {
                Toast.makeText(this, "Player wins!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Dealer wins!", Toast.LENGTH_SHORT).show()
            }
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            clearTable()
            returnToBetFragment()
        }, 3000)
    }
    fun clearTable() {
        gameIsActive = false

        dealerCards.clear()
        players.forEach { player ->
            player.hands.forEach { hand ->
                hand.cards.clear()
            }
        }

        dealerCardsImageViews.forEach { imageView ->
            imageView.setImageDrawable(null)
        }
    }
    fun returnToBetFragment() {
        resetDealerCardValue()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_gameplay_container, BetViewFragment())
            .commit()
    }

    fun resetDealerCardValue() {
        cardValueDealerTextView.text = ""
    }

}

