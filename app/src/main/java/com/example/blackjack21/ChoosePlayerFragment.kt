package com.example.blackjack21

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView




interface RecyclerViewEvent {
    fun onRowClick(position: Int)
}

class ChoosePlayerFragment : Fragment(), RecyclerViewEvent {
    private var playerData = mutableListOf<BlackJackPlayer>()

    private lateinit var backImageView: ImageView
    private lateinit var newUserTextView: TextView
    private lateinit var tapPlayerName : TextView

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
        val fragment = inflater.inflate(R.layout.fragment_choose_player, container, false)
        newUserTextView = fragment.findViewById(R.id.newUserTextView)
        backImageView = fragment.findViewById(R.id.backImageView)
        tapPlayerName = fragment.findViewById(R.id.tap_name_continue)
        newUserTextView.setOnClickListener{
            changeFragment(NewAndEditPlayerFragment())
        }
        backImageView.setOnClickListener {
            onBackPress()
        }
        playerData = getPlayerList()
        val recyclerView: RecyclerView = fragment.findViewById(R.id.playerRecyclerView)
        recyclerView.adapter = PlayerAdapter(this, playerData, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Animations.pulse2(tapPlayerName)
    }

    private fun getPlayerList(): MutableList<BlackJackPlayer> {
        val listOfPlayers = GameManager.players.values.toMutableList()
        return listOfPlayers
    }

    fun changeFragment(fragment: Fragment){
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)

        fragmentTransaction.commit()
    }

    private fun onBackPress() {
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack()
    }



    override fun onRowClick(position: Int) {
        activity?.let {
            val intent = Intent(requireActivity(), GameplayActivity::class.java)
            GameManager.setActivePlayer(playerData[position].name)
            startActivity(intent)
        }
    }

    fun onEditClick(name: String) {
        val fragment = NewAndEditPlayerFragment()
        val bundle = Bundle()
        bundle.putString("name", name)
        fragment.arguments = bundle
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)

        fragmentTransaction.commit()
    }

    companion object {

    }
}