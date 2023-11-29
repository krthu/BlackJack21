package com.example.blackjack21

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

private const val STARTING_MONEY = 1000.0

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewAndEditPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewAndEditPlayerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var nameTextEdit: EditText
    lateinit var saveTextView: TextView


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
        return inflater.inflate(R.layout.fragment_new_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameTextEdit = view.findViewById(R.id.newPlayerEditTextView)
        saveTextView = view.findViewById(R.id.saveNewPlayerTextView)
        val name = arguments?.getString("name", "")
        var backArrow = view.findViewById<ImageView>(R.id.backImageView)

        if (name != "" && name != null){
            val title = view.findViewById<TextView>(R.id.headlineTextView)
            title.text = "Edit Player"
            nameTextEdit.setText(name)
            saveTextView.setOnClickListener{
                updatePlayerName(name)
            }
        }else {
            saveTextView.setOnClickListener{
                savePlayer()
            }
        }
        backArrow.setOnClickListener{
            onBackPress()
        }

    }

    private fun updatePlayerName(oldName: String){

        if (GameManager.updatePlayerName(nameTextEdit.text.toString().trim(), oldName, requireContext())) {
            onBackPress()
        }
    }

    private fun savePlayer(){
        if (nameTextEdit.text.toString() != ""){
            val name = nameTextEdit.text.toString().trim()
          if (GameManager.saveNewPlayer(BlackJackPlayer(name, STARTING_MONEY), requireContext())){
                onBackPress()
          }else{

              Toast.makeText(requireContext(),"Name is taken!", Toast.LENGTH_SHORT).show()
          }
        }
    }

    private fun onBackPress(){
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
         * @return A new instance of fragment newPlayerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewAndEditPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}