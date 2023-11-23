package com.example.blackjack21

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text

class BetViewFragment : Fragment() {


    private var totalBet = 0
    private lateinit var placeBetButton: Button
    private lateinit var removeBetButton: Button
    private lateinit var betAmountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bet__view_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placeBetButton = view.findViewById(R.id.btn_placeBet)
        removeBetButton = view.findViewById(R.id.btn_removeBet)
        betAmountTextView = view.findViewById(R.id.betAmountTextView)


        placeBetButton.setOnClickListener {
            onPlaceBetClicked()
        }

        removeBetButton.setOnClickListener {
            removeBet()
        }

        updateBetButtonState()

        val betValues = mapOf(
            R.id.bet_value10 to 10,
            R.id.bet_value20 to 20,
            R.id.bet_value50 to 50,
            R.id.bet_value100 to 100
        )

        betValues.forEach { (imageViewId, beValue) ->
            setBetValue(imageViewId, beValue)
        }
    }
    private fun setBetValue(imageViewId: Int, beValue: Int) {
        val imageView: ImageView = requireView().findViewById(imageViewId)
        imageView.setOnClickListener {
            addBetValue(beValue)
        }
    }
    private fun addBetValue(value: Int) {
        totalBet += value
        updateBetButtonState()
        betAmountTextView.text = "$totalBet"
    }
    private fun removeBet() {
        totalBet = 0
        updateBetButtonState()
        betAmountTextView.text = "$totalBet"
    }
    private fun onPlaceBetClicked() {
        val gameplayFragment = GameplayFragment.newInstance(totalBet)
        (activity as? GameplayActivity)?.replaceFragment(gameplayFragment)
    }
    private fun updateBetButtonState() {
        if (totalBet == 0) {
            placeBetButton.alpha = 0.5f
            placeBetButton.isEnabled = false
        } else {
            placeBetButton.alpha = 1.0f
            placeBetButton.isEnabled = true
        }
    }

}