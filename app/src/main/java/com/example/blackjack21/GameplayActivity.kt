package com.example.blackjack21

import android.net.ipsec.ike.ChildSaProposal
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.isVisible
import kotlin.math.log

class GameplayActivity : AppCompatActivity(), GameplayFragment.GamePlayListener {

    lateinit var cardValueDealerTextView: TextView
    lateinit var cardValueDealerHolder: ImageView
    lateinit var cogWheelMenu: ImageView
    val players = mutableListOf<BlackJackPlayer>()
    val deck = Deck(7)
    val dealerCards = mutableListOf<Card>()
    val dealerCardsImageViews = mutableListOf<ImageView>()
    var isDealerTurn = false
    var gameIsActive = true
    var currentHandIndex = 0
    var hasSplit = false
    var hasDoubled = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameplay)

        setReferences()
        cardValueDealerHolder.visibility = View.INVISIBLE
        updatePlayerInfo()  // Uppdatera spelarinfo utan argument

        deck.shuffle()
        if (savedInstanceState == null) {
            val fragment = BetViewFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_gameplay_container, fragment)
                .commit()
        }

        cogWheelMenu = findViewById(R.id.cogwheel_gameplay)
        cogWheelMenu.setOnClickListener { view ->
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


    override fun onHitPress() {
        buttonsEnabled(false)
        val currentPlayer = GameManager.activePlayer ?: return
        val playerHand = currentPlayer.hands[currentHandIndex]
        playerHand.addCard(deck.drawACard())

        updatePlayerUI(currentHandIndex)


        val playerValue = getBlackJackValue(playerHand.cards)

        // stand of player busts
        if (playerValue > 21 || hasDoubled) {
            onStandPress()

        // Auto stand on 5 card Charlie and 21
        } else if (playerValue <= 21 && currentPlayer.hands[currentHandIndex].cards.size == 5 || playerValue == 21){
            onStandPress()
        } else {
            buttonsEnabled(true)
        }
    }


    override fun onStandPress() {
        buttonsEnabled(false)
        if (!hasSplit || currentHandIndex == 1){
            playDealerHand()
        } else{
            hasSplit == false
            currentHandIndex = 1
            val fragment =
                supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment

            if (fragment != null) {
                fragment.activeHandView.alpha = 0.5f
                fragment.activeHandView = fragment.handViewList[1]
                fragment.activeHandView.alpha = 1f
            }
            val handler = Handler(Looper.getMainLooper())
            val delayBetweenCards = 500L
            handler.postDelayed({
                onHitPress()
                updatePlayerUI()
                buttonsEnabled(true)
            }, delayBetweenCards)
            fragment?.doubleButton?.isVisible = true
            fragment?.doubleText?.isVisible = true
            fragment?.doubleIcon?.isVisible = true

        }
    }

    override fun onDoublePress() {

        val currentPlayer = GameManager.activePlayer

        val fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment
        if (GameManager.activePlayer?.doubleBetInHand(currentHandIndex) == true) {
            updatePlayerInfo()
            if (currentPlayer != null) {
                fragment?.updateBetValueText(currentPlayer.hands[currentHandIndex].bet.toInt(), currentHandIndex)
                hasDoubled = true
                onHitPress()
                onStandPress()
            }
        } else {
            Toast.makeText(this, "Not enough Money", Toast.LENGTH_SHORT)
        }
    }

    override fun onSplitPress() {
        if (GameManager.activePlayer?.split() == true){

            val fragment =
                supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment

            hasSplit = true
            updatePlayerInfo()
            fragment?.splitButton?.isVisible = false
            fragment?.view?.findViewById<TextView>(R.id.split_text)?.isVisible = false
            fragment?.view?.findViewById<ImageView>(R.id.split_icon)?.isVisible = false
            val handler = Handler(Looper.getMainLooper())
            val delayBetweenCards = 500L //
            fragment?.activeHandView?.cardImageViews?.get(1)?.setImageResource(0)


            handler.postDelayed({
                updatePlayerUI(1)

            }, delayBetweenCards)
            handler.postDelayed({
                // Card for the old hand
                onHitPress()

                updatePlayerUI()
            }, delayBetweenCards * 2 )



            handler.postDelayed({
                if (fragment?.handsContainer?.getChildAt(0)?.alpha == 1f){
                    fragment?.handsContainer?.getChildAt(1)?.alpha = 0.5f
                }
            }, delayBetweenCards*3)
        }else{
            Toast.makeText(this, "Not enough Money", Toast.LENGTH_SHORT).show()
        }
    }

    fun buttonsEnabled(enabled: Boolean){
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment
        fragment?.doubleButton?.isEnabled = enabled
        fragment?.hitButton?.isEnabled = enabled
        fragment?.standButton?.isEnabled = enabled
        fragment?.splitButton?.isEnabled = enabled

    }

    private fun dealInitialCards() {
        deck.stackDeck()
        val currentPlayer = GameManager.activePlayer ?: return
        val handler = Handler(Looper.getMainLooper())
        val delayBetweenCards = 500L //


        handler.postDelayed({
            val playerFirstCard = deck.drawACard()
            currentPlayer.addCard(currentHandIndex, playerFirstCard)
            updatePlayerUI()
        }, delayBetweenCards)


        handler.postDelayed({
            val dealerFirstCard = deck.drawACard()
            dealerCards.add(dealerFirstCard)
            updateDealerCardImages(dealerCards)
        }, delayBetweenCards * 2)


        handler.postDelayed({
            val playerSecondCard = deck.drawACard()
            currentPlayer.addCard(currentHandIndex, playerSecondCard)
            updatePlayerUI()
            if (currentPlayer.ableToSplit()){
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment

                fragment?.splitButton?.isVisible = true
                fragment?.view?.findViewById<TextView>(R.id.split_text)?.isVisible = true
                fragment?.view?.findViewById<ImageView>(R.id.split_icon)?.isVisible = true
            }
        }, delayBetweenCards * 3)


        handler.postDelayed({
            val dealerSecondCard = deck.drawACard()
            dealerCards.add(dealerSecondCard)
            dealerCardsImageViews.getOrNull(1)?.setImageResource(R.drawable.card_back)
            updateDealerCardImages(dealerCards)
            buttonsEnabled(true)


            if (getBlackJackValue(currentPlayer.hands[currentHandIndex].cards) == 21) {
                //checkBlackJack()
                onStandPress()

            }
        }, delayBetweenCards * 4)

    }

    private fun updatePlayerUI(indexOfHandToUpdate: Int = currentHandIndex) {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment
        fragment?.updatePlayerCards(
            GameManager.activePlayer?.hands?.getOrNull(indexOfHandToUpdate)?.cards ?: emptyList(), indexOfHandToUpdate
        )
        fragment?.updatePlayerCardValue(
            getBlackJackValue(
                GameManager.activePlayer?.hands?.getOrNull(
                    indexOfHandToUpdate
                )?.cards ?: emptyList()
            ), indexOfHandToUpdate
        )
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

    fun updatePlayerInfo() {
        val currentPlayer = GameManager.activePlayer
        if (currentPlayer != null) {
            val currentPlayerInfoBar: TextView = findViewById(R.id.currentPlayer_infoBar)
            val totalMoneyInfoBar: TextView = findViewById(R.id.totalMoney_infoBar)

            currentPlayerInfoBar.text = "Current player: ${currentPlayer.name}"
            totalMoneyInfoBar.text = "Cash: ${currentPlayer.money}"
        }
    }


    fun replaceFragment(gameplayFragment: GameplayFragment) {
        gameIsActive = true
        deck.shuffle()

        dealInitialCards()
        updateDealerCardImages(dealerCards)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_gameplay_container, gameplayFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setReferences() {
        dealerCardsImageViews.add(findViewById(R.id.first_card_dealer))
        dealerCardsImageViews.add(findViewById(R.id.second_card_dealer))
        dealerCardsImageViews.add(findViewById(R.id.third_card_dealer))
        dealerCardsImageViews.add(findViewById(R.id.fourth_card_dealer))
        dealerCardsImageViews.add(findViewById(R.id.fifth_card_dealer))
        dealerCardsImageViews.add(findViewById(R.id.sixth_card_dealer))
        cardValueDealerTextView = findViewById(R.id.card_value_dealer)
        cardValueDealerHolder = findViewById(R.id.dealer_valueHolder)
    }

    private fun updateDealerCardImages(cards: List<Card>) {
        if (cards.isNotEmpty()) {
            cards.forEachIndexed { index, card ->
                val imageName = if (!isDealerTurn && index == 1) "card_back" else card.imageString
                val imageId = resources.getIdentifier(imageName, "drawable", packageName)
                dealerCardsImageViews.getOrNull(index)?.setImageResource(imageId)
            }
        }
    }

    private fun updateDealerCardsValue(cards: List<Card>) {
        var value = 0
        if (cards.isNotEmpty()) {
            if (!isDealerTurn) {
                value = getBlackJackValue(listOf(cards[0]))
            } else {
                value = getBlackJackValue(cards)
            }
        }

        cardValueDealerTextView.text = value.toString()
        cardValueDealerHolder.visibility = View.VISIBLE
    }

    fun playDealerHand() {
        if (currentHandIndex == 1){
            hasDoubled = false
            val fragment =
                supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment
            fragment?.handsContainer?.getChildAt(0)?.alpha = 1f
        }
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

    private fun checkWinner() {
        val moneyBefore = GameManager.activePlayer?.money
        for (i in 0..currentHandIndex){
            val currentPlayer = GameManager.activePlayer ?: return
            val playerValue = getBlackJackValue(currentPlayer.hands[i].cards)
            val dealerValue = getBlackJackValue(dealerCards)
            val betAmount = currentPlayer.hands[i].getBetAmount()
            Log.d("!!!", "Player: $playerValue Dealer: $dealerValue")
            when {
                // Player Bust
                playerValue > 21 -> {
                    Log.d("!!!", "Player: $playerValue Dealer: $dealerValue Player Bust")
                }

                // Someone has Black Jack
                playerValue == 21 && currentPlayer.hands[i].cards.size == 2 || dealerValue == 21 && dealerCards.size == 2 ->{
                    when{
                        // Both have Black Jack
                        dealerValue == 21 && dealerCards.size == 2 && playerValue == 21 && currentPlayer.hands[i].cards.size == 2 -> {
                            currentPlayer.addMoney(betAmount)
                            Log.d("!!!", "Player: $playerValue Dealer: $dealerValue Both BJ")
                        }
                        // Dealer has Black Jack
                        dealerValue == 21 && dealerCards.size == 2 -> {
                            Log.d("!!!", "Player: $playerValue Dealer: $dealerValue Dealer has BJ")
                        }
                        // Player had Black Jack
                        else -> {
                            currentPlayer.addMoney(betAmount * 2.5)
                            Log.d("!!!", "Player: $playerValue Dealer: $dealerValue Player has BJ")
                        }
                    }

                }
                // Dealer bust or player win or player has 5 card charlie
                dealerValue > 21 || playerValue > dealerValue || playerValue <= 21 && currentPlayer.hands[i].cards.size == 5 -> {

                    currentPlayer.addMoney(betAmount * 2)
                    Log.d("!!!", "Player: $playerValue Dealer: $dealerValue Player Wins")
                }
                // Tie game
                playerValue == dealerValue -> {
                    Log.d("!!!", "Player: $playerValue Dealer: $dealerValue Tie")
                    currentPlayer.addMoney(betAmount)
                }
                // Dealer Win
                else -> {
                    Log.d("!!!", "Player: $playerValue Dealer: $dealerValue Dealer wins")
                }
            }
        }
        if (moneyBefore != null) {
            Log.d("!!!", "Money changed ${GameManager.activePlayer?.money!! - moneyBefore}")
        }

        cleanUpGame()
    }


    private fun cleanUpGame() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ resetGame() }, 3000)
        updatePlayerInfo()
    }


    // Help method for the dealer to check for BJ if the visible card is Ace or 10
    private fun Card.isFaceCardOrAce(): Boolean {
        return this.number == 14 || (this.number in 11..13)
    }

    private fun clearTable() {
        dealerCards.clear()
        players.forEach { player ->
            player.clearHands()
        }

        dealerCardsImageViews.forEach { imageView ->
            imageView.setImageDrawable(null)
        }
    }

    private fun returnToBetFragment() {
        resetDealerCardValue()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_gameplay_container, BetViewFragment())
            .commit()
    }

    private fun resetDealerCardValue() {
        cardValueDealerTextView.text = ""
        cardValueDealerHolder.visibility = View.INVISIBLE
    }

    private fun resetGame() {
        val currentPlayer = GameManager.activePlayer
        gameIsActive = false
        isDealerTurn = false
        hasSplit = false
        hasDoubled = false
        currentHandIndex = 0
        clearTable()

        currentPlayer?.clearHands()

        dealerCards.clear()
        updateDealerCardImages(dealerCards)
        GameManager.savePlayersToSharedPref(this)
        returnToBetFragment()
    }
}

