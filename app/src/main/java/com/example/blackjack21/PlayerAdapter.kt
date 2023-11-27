package com.example.blackjack21

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerAdapter(
    private val data: MutableList<BlackJackPlayer>,
    private val lister: RecyclerViewEvent
) : RecyclerView.Adapter<PlayerAdapter.ItemViewHoler>() {

    // Setup variables to hold instances of views
    // Kind like onCreate
    inner class ItemViewHoler(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
       init {
           view.setOnClickListener(this)
       }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                lister.onRowClick(position)
            }
        }

        val nameTextView: TextView = view.findViewById(R.id.nameView)
        val moneyTextView: TextView = view.findViewById(R.id.moneyView)
    }
// Inflating the layout of each row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHoler {
        val inlfatedView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.choose_player_row, parent,false)
        return ItemViewHoler(inlfatedView)
    }

// returns how many item there is
    override fun getItemCount(): Int {
        return data.size
    }

    // Assign values on rows
    override fun onBindViewHolder(holder: ItemViewHoler, position: Int) {
        val player: BlackJackPlayer = data[position]
        holder.nameTextView.text = player.name.replaceFirstChar { it.uppercase() }
        holder.moneyTextView.text = player.money.toString()
    }
}