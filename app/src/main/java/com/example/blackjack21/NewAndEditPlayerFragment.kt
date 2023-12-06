package com.example.blackjack21

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

private const val STARTING_MONEY = 1000.0

class NewAndEditPlayerFragment : Fragment() {

    lateinit var nameTextEdit: EditText
    lateinit var saveTextView: TextView
    lateinit var errorTextView: TextView


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
        return inflater.inflate(R.layout.fragment_new_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameTextEdit = view.findViewById(R.id.newPlayerEditTextView)
        saveTextView = view.findViewById(R.id.saveNewPlayerTextView)
        errorTextView = view.findViewById(R.id.errorTextView)
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
        if (nameTextEdit.text.toString() != ""){
            if (nameTextEdit.text.length < 10){
                if (GameManager.updatePlayerName(nameTextEdit.text.toString().trim(), oldName, requireContext())) {
                    onBackPress()
                }
                else{
                    showErrorView(getString(R.string.name_is_taken))
                }

            }else{
                showErrorView(getString(R.string.name_is_to_long))
            }
        }
    }

    private fun showErrorView(errorText: String){
        errorTextView.text = errorText
        Animations.fadeInAndOut(errorTextView)
    }

    private fun savePlayer(){
        if (nameTextEdit.text.toString() != "") {

            if (nameTextEdit.text.toString().length < 10){
                val name = nameTextEdit.text.toString().trim()
                if (GameManager.saveNewPlayer(BlackJackPlayer(name, STARTING_MONEY), requireContext())){
                    onBackPress()
                }else{
                    showErrorView(getString(R.string.name_is_taken))
                }
            }else{
                showErrorView(getString(R.string.name_is_to_long))
            }
        }
    }

    private fun onBackPress(){
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack()
    }


    companion object {

    }
}