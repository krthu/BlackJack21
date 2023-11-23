package com.example.blackjack21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class GameplayActivity : AppCompatActivity(), GameplayFragment.GamePlayListener {
    val players = mutableListOf<BlackJackPlayer>()
    val deck = Deck(7)
    val dealerCards = mutableListOf<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameplay)

        addPlayer("Nils", 100) // Byt mot info from intent
        players[0].makeBet(10)
        deck.shuffle()
        dealInitialCards()

        if (savedInstanceState == null) {
            val fragment = BetViewFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_gameplay_container, fragment)
                .commit()
        }

    }

    fun replaceFragment(gameplayFragment: GameplayFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_gameplay_container, gameplayFragment)
            .addToBackStack(null)
            .commit()
    }

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

    fun playDealerHand(){
        while (getBlackJackValue(dealerCards) < 17){
            dealerCards.add(deck.drawACard())
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
  override fun updatePlayerCards(){
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_gameplay_container) as? GameplayFragment
        fragment?.updatePlayerCards(players[0].hands[0].cards)
        fragment?.updatePlayerCardValue(getBlackJackValue(players[0].hands[0].cards))
    }
    override fun onHitPress() {
        players[0].hands[0].addCard(deck.drawACard())
        updatePlayerCards()
    }
}