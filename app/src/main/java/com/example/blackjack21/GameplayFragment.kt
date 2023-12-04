package com.example.blackjack21

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible


class GameplayFragment : Fragment() {

    private var totalBet = 0

    interface GamePlayListener {
        fun getBlackJackValue(cards: List<Card>): Int
        fun onHitPress()
        fun onStandPress()
      //  fun updatePlayerCards()

        fun onDoublePress()

        fun onSplitPress()
    }
    
    private var listener: GamePlayListener? = null
    lateinit var hitButton: Button
    lateinit var standButton : Button
    lateinit var doubleButton: Button
    lateinit var handsContainer: LinearLayout
    lateinit var splitButton: Button

    lateinit var playerCardValueTextView: TextView
    lateinit var betValueTextView: TextView
    lateinit var firstHand: PlayerHandView
    private val playerCardImages = mutableListOf<ImageView>()
    lateinit var activeHandView: PlayerHandView
    var handViewList = mutableListOf<PlayerHandView>()


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
            totalBet = it.getInt("totalBet")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragment = inflater.inflate(R.layout.fragment_gameplay, container, false)




        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setReferances(view)
        activeHandView.setBetText("$totalBet")
        standButton.setOnClickListener {
            listener?.onStandPress()
        }
        hitButton.setOnClickListener{
            doubleButton.isVisible = false
            splitButton.isVisible = false
            listener?.onHitPress()
        }

        doubleButton.setOnClickListener{
            listener?.onDoublePress()
        }

        splitButton.setOnClickListener{
            createNewHandView()
            listener?.onSplitPress()

        }

        standButton.isEnabled = false
        hitButton.isEnabled = false
        doubleButton.isEnabled = false
        splitButton.isVisible = false
    }

    fun createNewHandView(){
        val newHand = PlayerHandView(requireContext())
        newHand.id = View.generateViewId()
        newHand.setBetText("$totalBet")
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1f
        )
        newHand.layoutParams = layoutParams

        handsContainer.addView(newHand)
        handsContainer.requestLayout()
        handViewList.add(newHand)
    }

    fun setReferances(fragment: View){
        hitButton = fragment.findViewById(R.id.btn_hit)
        standButton = fragment.findViewById(R.id.btn_stand)
        doubleButton = fragment.findViewById(R.id.btn_double)
        splitButton = fragment.findViewById(R.id.btn_split)
        playerCardValueTextView = fragment.findViewById(R.id.card_value_player)
        betValueTextView = fragment.findViewById(R.id.bet_amount_player)
        handsContainer = fragment.findViewById(R.id.handsContainer)
        playerCardImages.add(fragment.findViewById(R.id.first_card_player))
        playerCardImages.add(fragment.findViewById(R.id.second_card_player))
        playerCardImages.add(fragment.findViewById(R.id.third_card_player))
        playerCardImages.add(fragment.findViewById(R.id.fourth_card_player))
        playerCardImages.add(fragment.findViewById(R.id.fifth_card_player))
        firstHand = fragment.findViewById(R.id.first_hand)
        handViewList.add(firstHand)
        activeHandView = firstHand


    }
    fun updatePlayerCardValue(value: Int, handIndex: Int){
        handViewList[handIndex].setValueText(value)
    }
    fun updatePlayerCards(cards:List<Card>, handIndex: Int){
        handViewList[handIndex].setImage(cards)

    }

    fun updateBetValueText(betAmount: Int, handIndex: Int){
        handViewList[handIndex].setBetText(betAmount.toString())
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