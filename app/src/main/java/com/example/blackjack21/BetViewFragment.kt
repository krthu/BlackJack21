package com.example.blackjack21

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class BetViewFragment : Fragment() {

    private lateinit var fadeInAnimation: Animation
    private lateinit var fadeOutAnimation: Animation

    private var totalBet = 0
    private lateinit var placeBetButton: Button
    private lateinit var removeBetButton: Button
    private lateinit var betAmountTextView: TextView
    private lateinit var placeYourBetText: TextView
    private var isFirstBet = true

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

        // Load animations
        fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out)

        placeBetButton = view.findViewById(R.id.btn_placeBet)
        removeBetButton = view.findViewById(R.id.btn_removeBet)
        betAmountTextView = view.findViewById(R.id.betAmountTextView)
        placeYourBetText = view.findViewById(R.id.placeYourBetView)


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

        // Initial visibility setup
        placeYourBetText.visibility = View.VISIBLE
        betAmountTextView.visibility = View.GONE
        placeBetButton.visibility = View.GONE
        removeBetButton.visibility = View.GONE
    }
    private fun setBetValue(imageViewId: Int, betValue: Int) {
        val imageView: ImageView = requireView().findViewById(imageViewId)
        imageView.setOnClickListener {
            addBetValue(betValue)
            updateTotalBetChipImage(betValue)
            // Change visibility upon bet selection and applying animations
            if (isFirstBet) {
                placeYourBetText.visibility = View.GONE
                applyVisibilityAnimation(betAmountTextView, true)
                applyVisibilityAnimation(placeBetButton, true)
                applyVisibilityAnimation(removeBetButton, true)

                isFirstBet = false
            }
        }
    }

    private fun updateTotalBetChipImage(betValue: Int) {
        val totalBetChip: ImageView = requireView().findViewById(R.id.total_bet_chip)
        val resourceID = when(betValue) {
            10 -> R.drawable.chip_value10
            20 -> R.drawable.chip_value20
            50 -> R.drawable.chip_value50
            100 -> R.drawable.chip_value100
            else -> 0
        }
        if (resourceID != 0) {
            totalBetChip.setImageResource(resourceID)
        }

    }

    private fun applyVisibilityAnimation(view: View, makeVisible: Boolean) {
        if (makeVisible) {
            view.startAnimation(fadeInAnimation)
            view.visibility = View.VISIBLE
        } else {
            view.startAnimation(fadeOutAnimation)
            view.visibility = View.GONE
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
        // Change to the initial visibility
        placeYourBetText.visibility = View.VISIBLE
        betAmountTextView.visibility = View.GONE
        placeBetButton.visibility = View.GONE
        removeBetButton.visibility = View.GONE
        isFirstBet = true
    }
    private fun onPlaceBetClicked() {
        val player = GameManager.activePlayer
        if (player != null && player.makeBet(totalBet.toDouble())) {
            val gameplayFragment = GameplayFragment.newInstance(totalBet)
            (activity as? GameplayActivity)?.apply {
                replaceFragment(gameplayFragment)
                updatePlayerInfo()  // Uppdatera spelarinformationen efter att satsningen har gjorts
            }
        } else {
            Toast.makeText(context, "Not enough money to make this bet", Toast.LENGTH_SHORT).show()
        }
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