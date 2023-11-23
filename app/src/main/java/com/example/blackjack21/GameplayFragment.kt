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

/**
 * A simple [Fragment] subclass.
 * Use the [GameplayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameplayFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var hitButton: Button
    lateinit var playerCard1: ImageView
    lateinit var playerCard2: ImageView
    lateinit var playerCard3: ImageView
    lateinit var playerCard4: ImageView
    lateinit var playerCard5: ImageView
    lateinit var playerCardValueTextView: TextView
    private val playerCardImages = mutableListOf<ImageView>()
    val game = Game()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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

    fun setReferances(fragment: View){
        playerCard1 = fragment.findViewById(R.id.first_card_player)
        playerCard2 = fragment.findViewById(R.id.second_card_player)
        playerCard3 = fragment.findViewById(R.id.third_card_player)
        playerCard4 = fragment.findViewById(R.id.fourth_card_player)
        playerCard5 = fragment.findViewById(R.id.fifth_card_player)
        hitButton = fragment.findViewById(R.id.btn_hit)
        playerCardValueTextView = fragment.findViewById(R.id.card_value_player)

        playerCardImages.add(playerCard1)
        playerCardImages.add(playerCard2)
        playerCardImages.add(playerCard3)
        playerCardImages.add(playerCard4)
        playerCardImages.add(playerCard5)


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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameplayFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameplayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}