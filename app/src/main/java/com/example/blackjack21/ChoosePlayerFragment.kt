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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var playerData = mutableListOf<BlackJackPlayer>()
/**
 * A simple [Fragment] subclass.
 * Use the [ChoosePlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChoosePlayerFragment : Fragment(), RecyclerViewEvent {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var backImageView: ImageView
    private lateinit var newUserTextView: TextView
    private lateinit var tapPlayerName : TextView

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
        val fragment = inflater.inflate(R.layout.fragment_choose_player, container, false)
        newUserTextView = fragment.findViewById(R.id.newUserTextView)
        backImageView = fragment.findViewById(R.id.backImageView)
        tapPlayerName = fragment.findViewById(R.id.tap_name_continue)
        newUserTextView.setOnClickListener{ changeFragment(NewAndEditPlayerFragment()) }
        backImageView.setOnClickListener { onBackPress() }
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChoosePlayerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChoosePlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
}