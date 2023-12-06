package com.example.blackjack21

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainMenuFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var startGameView: TextView
    private lateinit var scoreBoardView: TextView
    private lateinit var quitView: TextView
    private lateinit var flagView: ImageView
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainMenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainMenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}