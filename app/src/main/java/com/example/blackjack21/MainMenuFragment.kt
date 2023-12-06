package com.example.blackjack21

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.util.Locale


class MainMenuFragment : Fragment() {

    private lateinit var startGameView: TextView
    private lateinit var scoreBoardView: TextView
    private lateinit var quitView: TextView
    private lateinit var flagView: ImageView
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
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startGameView = view.findViewById(R.id.startGameView)
        scoreBoardView = view.findViewById(R.id.scoreBoardView)
        quitView = view.findViewById(R.id.quitView)
        flagView = view.findViewById(R.id.flagImage)

        startGameView.setOnClickListener { onStartPress() }
        scoreBoardView.setOnClickListener { onScoreBoardPress() }
        quitView.setOnClickListener { onQuitPress() }
        flagView.setOnClickListener { toggleLanguage() }

        if (GameManager.currentLanguage == "en") {
            flagView.setImageResource(R.drawable.en_flag)
        } else {
            flagView.setImageResource(R.drawable.swe_flag)
        }

    }
    fun changeFragment(fragment: Fragment){
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)

        fragmentTransaction.commit()
    }

    fun onStartPress(){
        changeFragment(ChoosePlayerFragment())
    }

    private fun toggleLanguage() {
        if (GameManager.currentLanguage == "en") {
            flagView.setImageResource(R.drawable.swe_flag)
            setLocale("sv")
            GameManager.currentLanguage = "sv"
        } else {
            flagView.setImageResource(R.drawable.en_flag)
            setLocale("en")
            GameManager.currentLanguage = "en"
        }
        restartActivity()
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = context?.resources
        val config = resources?.configuration
        config?.locale = locale
        resources?.updateConfiguration(config, resources.displayMetrics)
    }

    fun restartActivity() {
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }



    fun onScoreBoardPress(){
        changeFragment(ScoreBoardFragment())
    }

    fun onQuitPress(){

        requireActivity().finish();
    }

    companion object {

    }
}