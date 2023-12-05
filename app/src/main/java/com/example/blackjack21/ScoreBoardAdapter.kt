package com.example.blackjack21

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScoreBoardAdapter(
    val data: MutableList<BlackJackPlayer>,
    val context: Context
) : RecyclerView.Adapter<ScoreBoardAdapter.ViewHolder>() {

    val layoutInflater = LayoutInflater.from(context)
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val placmentTextView = view.findViewById<TextView>(R.id.placementTextView)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val moneyTextView = view.findViewById<TextView>(R.id.moneyTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val itemView = layoutInflater.inflate(R.layout.scoreboard_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = data.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = data[position]
        holder.placmentTextView.text = (position+1).toString()
        holder.nameTextView.text = player.name
        holder.moneyTextView.text = player.money.toString()

        Animations.smallToBigAnimation(holder.itemView)
    }


}