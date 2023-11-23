package com.example.blackjack21

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class GameplayFragment : Fragment() {
    private var totalBet = 0
    lateinit var hitButton: Button
    lateinit var playerCardValueTextView: TextView
    private val playerCardImages = mutableListOf<ImageView>()
    val game = Game()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            totalBet = it.getInt("totalBet")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragment = inflater.inflate(R.layout.fragment_gameplay, container, false)
        setReferances(fragment)
        game.deck.shuffle()
        game.addPlayer("Nils", 100)
        game.players[0].makeBet(10)
        game.dealInitialCards()
        updatePlayerCards(game.players[0].hands[0].cards)
        updatePlayerCardValue(game.getBlackJackValue(game.players[0].hands[0].cards))
        hitButton.setOnClickListener{
            onHitPress()
        }
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val betAmountTextView: TextView = view.findViewById(R.id.bet_amount_player)
        betAmountTextView.text = "$totalBet"
    }

    fun setReferances(fragment: View){
        hitButton = fragment.findViewById(R.id.btn_hit)
        playerCardValueTextView = fragment.findViewById(R.id.card_value_player)

        playerCardImages.add(fragment.findViewById(R.id.first_card_player))
        playerCardImages.add(fragment.findViewById(R.id.second_card_player))
        playerCardImages.add(fragment.findViewById(R.id.third_card_player))
        playerCardImages.add(fragment.findViewById(R.id.fourth_card_player))
        playerCardImages.add(fragment.findViewById(R.id.fifth_card_player))


    }
    fun onHitPress(){
        game.players[0].addCard(0, game.deck.drawACard())
        val cards = game.players[0].hands[0].cards
        updatePlayerCards(cards)
        updatePlayerCardValue(game.getBlackJackValue(cards))
    }
    fun updatePlayerCardValue(value: Int){
        playerCardValueTextView.text = value.toString()
    }
    fun updatePlayerCards(cards:List<Card>){
        cards.forEachIndexed{index, card ->
            val imageId = resources.getIdentifier(getImageId(card), "drawable", requireActivity().packageName)
            playerCardImages[index].setImageResource(imageId)
        }
    }
    fun getImageId(card: Card): String{
        val builder = StringBuilder()
        when (card.suit){
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
        if (card.number < 10){
            builder.append(0)
        }
        builder.append(card.number)
        return builder.toString()
    }

    companion object {
        fun newInstance(totalBet: Int): GameplayFragment {
            val fragment = GameplayFragment()
            val args = Bundle()
            args.putInt("totalBet", totalBet)
            fragment.arguments = args
            return fragment
        }
    }

}