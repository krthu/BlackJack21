package com.example.blackjack21

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
import androidx.core.view.isVisible

class GameplayActivity : AppCompatActivity(), GameplayFragment.GamePlayListener {

    lateinit var cardValueDealerTextView: TextView
    lateinit var cogWheelMenu: ImageView
    val players = mutableListOf<BlackJackPlayer>()
    val deck = Deck(7)
    val dealerCards = mutableListOf<Card>()
    val dealerCardsImageViews = mutableListOf<ImageView>()
    var isDealerTurn = false
    var gameIsActive = true
    var currentHandIndex = 0
    var hasSplit = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameplay)

        updatePlayerInfo()  // Uppdatera spelarinfo utan argument

        setReferences()
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

    override fun updatePlayerCards() {
        val currentPlayer = GameManager.activePlayer
        val fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment
        if (currentPlayer != null) {
            fragment?.updatePlayerCards(currentPlayer.hands[currentHandIndex].cards)
        }
        if (currentPlayer != null) {
            fragment?.updatePlayerCardValue(getBlackJackValue(currentPlayer.hands[currentHandIndex].cards))
        }
    }

    override fun onHitPress() {
        val currentPlayer = GameManager.activePlayer ?: return
        val playerHand = currentPlayer.hands[currentHandIndex]
        buttonsEnabled(false)
        playerHand.addCard(deck.drawACard())
        updatePlayerCards()


        val playerValue = getBlackJackValue(playerHand.cards)
        if (playerValue > 21) {
           checkWinner()
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
                fragment.activeHandView = (fragment.handsContainer.getChildAt(1) as? PlayerHandView)!!
            }
        }
    }

    override fun onDoublePress() {

        val currentPlayer = GameManager.activePlayer

        val fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment
        if (GameManager.activePlayer?.doubleBetInHand(currentHandIndex) == true) {
            updatePlayerInfo()
            if (currentPlayer != null) {
                fragment?.updateBetValueText(currentPlayer.hands[currentHandIndex].bet.toInt())
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
            val handler = Handler(Looper.getMainLooper())
            val delayBetweenCards = 500L //


            handler.postDelayed({
                val cardForFirstHand = deck.drawACard()
                GameManager.activePlayer?.addCard(currentHandIndex, cardForFirstHand)
                updatePlayerUI()
            }, delayBetweenCards)

            handler.postDelayed({

                val secondHand = fragment?.handsContainer?.getChildAt(1) as? PlayerHandView
                if (secondHand != null) {
                    secondHand.setImage(GameManager.activePlayer!!.hands[1].cards)
                    secondHand.setValueText(getBlackJackValue(GameManager.activePlayer!!.hands[1].cards))
                }
            }, delayBetweenCards)
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
            }
        }, delayBetweenCards * 3)


        handler.postDelayed({
            val dealerSecondCard = deck.drawACard()
            dealerCards.add(dealerSecondCard)
            dealerCardsImageViews.getOrNull(1)?.setImageResource(R.drawable.card_back)
            updateDealerCardImages(dealerCards)
            buttonsEnabled(true)


            if (getBlackJackValue(currentPlayer.hands[currentHandIndex].cards) == 21) {
                checkBlackJack()

            }
        }, delayBetweenCards * 4)

    }

    private fun updatePlayerUI() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment
        fragment?.updatePlayerCards(
            GameManager.activePlayer?.hands?.getOrNull(currentHandIndex)?.cards ?: emptyList()
        )
        fragment?.updatePlayerCardValue(
            getBlackJackValue(
                GameManager.activePlayer?.hands?.getOrNull(
                    currentHandIndex
                )?.cards ?: emptyList()
            )
        )
    }


    private fun checkBlackJack() {
        val currentPlayer = GameManager.activePlayer ?: return
        val playerBetAmount = currentPlayer.hands[currentHandIndex].getBetAmount()
        val playerHasBlackJack =
            getBlackJackValue(currentPlayer.hands[currentHandIndex].cards) == 21 && currentPlayer.hands[currentHandIndex].cards.size == 2
        val dealerHasBlackJack = getBlackJackValue(dealerCards) == 21 && dealerCards.size == 2

        when {
            playerHasBlackJack && dealerHasBlackJack -> {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    currentPlayer.addMoney(playerBetAmount)

                    isDealerTurn = true

                    updateDealerCardImages(dealerCards)
                    updateDealerCardsValue(dealerCards)
                    cleanUpGame()
                }, 500)

            }

            playerHasBlackJack -> {
                currentPlayer.addMoney(playerBetAmount * 2.5)

                cleanUpGame()
            }

//            dealerHasBlackJack -> {
//                Log.d("!!!", "DealerBJ")
//                isDealerTurn = true
//                updateDealerCardImages(dealerCards)
//                updateDealerCardsValue(dealerCards)
//                cleanUpGame()
//            }

            else -> {
                // Ingen har Blackjack, spelet fortsätter
            }
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


    }

    /* fun addPlayer(name: String, money: Int) {
        players.add(BlackJackPlayer(name, money))
    } */

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
    }


    /* fun getPlayerBets() {

        players.forEach { player ->
            player.makeBet(50)
        }
    } */


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

    private fun checkWinner() {
        val moneyBefore = GameManager.activePlayer?.money
        for (i in 0..currentHandIndex){
            val currentPlayer = GameManager.activePlayer ?: return
            val playerValue = getBlackJackValue(currentPlayer.hands[i].cards)
            val dealerValue = getBlackJackValue(dealerCards)
            val betAmount = currentPlayer.hands[i].getBetAmount()
            Log.d("!!!", "Player: $playerValue Dealer: $dealerValue")
            when {
                playerValue > 21 -> {
                    Log.d("!!!", "Player: $playerValue Dealer: $dealerValue Player Bust")
                }

                dealerValue == 21 && dealerCards.size == 2 -> {

                }

                dealerValue > 21 || playerValue > dealerValue -> {

                    currentPlayer.addMoney(betAmount * 2)
                    Log.d("!!!", "Player: $playerValue Dealer: $dealerValue Player Wins")
                }

                playerValue == dealerValue -> {
                    Log.d("!!!", "Player: $playerValue Dealer: $dealerValue Tie")
                    currentPlayer.addMoney(betAmount)
                }

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
    }

    private fun resetGame() {
        val currentPlayer = GameManager.activePlayer
        gameIsActive = false
        isDealerTurn = false
        hasSplit = false
        currentHandIndex = 0
        clearTable()

        currentPlayer?.clearHands()

        dealerCards.clear()
        updateDealerCardImages(dealerCards)
        GameManager.savePlayersToSharedPref(this)
        returnToBetFragment()
    }


}

