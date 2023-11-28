package com.example.blackjack21

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView

class PlayerAdapter(
    private val data: MutableList<BlackJackPlayer>,
    private val lister: RecyclerViewEvent
) : RecyclerView.Adapter<PlayerAdapter.ItemViewHolder>() {
    var isSwipeEnabled = false
    // Setup variables to hold instances of views
    // Kind like onCreate
    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{

        val nameTextView: TextView = view.findViewById(R.id.nameView)
        val moneyTextView: TextView = view.findViewById(R.id.moneyView)
        val editImageView: ImageView = view.findViewById(R.id.editImageView)
        val deleteImageView: ImageView = view.findViewById(R.id.deleteImageView)
       init {
           view.setOnClickListener(this)

           deleteImageView.setOnClickListener{
                deletePlayer(adapterPosition, view.context)
           }
       }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                lister.onRowClick(position)
            }
        }


    }

    fun deletePlayer(position: Int, context: Context){
        val name = data[position].name
        val dataManager = SaveDataManager(context)
        dataManager.deletePlayer(name)
        data.removeAt(position)
        notifyItemRemoved(position)
    }
// Inflating the layout of each row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflatedView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.choose_player_row, parent,false)
        return ItemViewHolder(inflatedView)
    }

// returns how many item there is
    override fun getItemCount(): Int {
        return data.size
    }

    // Assign values on rows
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val player: BlackJackPlayer = data[position]
        holder.nameTextView.text = player.name.replaceFirstChar { it.uppercase() }
        holder.moneyTextView.text = "$${player.money.toString()}"
    }
}