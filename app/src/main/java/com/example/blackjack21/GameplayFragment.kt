package com.example.blackjack21

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

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
    interface GamePlayListener {
        fun getActivePlayer(): BlackJackPlayer
        fun getBlackJackValue(cards: List<Card>): Int

        fun onHitPress()

        fun getActiveDeck(): Deck

    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: GamePlayListener? = null
    lateinit var hitButton: Button
    lateinit var playerCardValueTextView: TextView
    private val playerCardImages = mutableListOf<ImageView>()
    var currentPlayer: BlackJackPlayer? = null
    var deck: Deck? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GamePlayListener) {
            listener = context

        } else {
            throw RuntimeException("$context must implement GameplayListener")
        }
    }


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

        currentPlayer = listener?.getActivePlayer()
        deck = listener?.getActiveDeck()

        if (currentPlayer != null) {
            Toast.makeText(requireContext(), "In",Toast.LENGTH_SHORT)
            updatePlayerCards(currentPlayer!!.hands[0].cards)
            listener?.let { updatePlayerCardValue(it.getBlackJackValue(currentPlayer!!.hands[0].cards)) }

        }


        hitButton.setOnClickListener{
            onHitPress()
        }
        return fragment

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
        deck?.drawACard()?.let { currentPlayer?.hands?.get(0)?.addCard(it) }
        val cards = currentPlayer?.hands?.get(0)?.cards
        if (cards != null) {
            updatePlayerCards(cards)
        }
        listener?.let { cards?.let { it1 -> it.getBlackJackValue(it1) }
            ?.let { it2 -> updatePlayerCardValue(it2) } }
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