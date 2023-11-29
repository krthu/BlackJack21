package com.example.blackjack21

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
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
    private val fragment: ChoosePlayerFragment,
    private val data: MutableList<BlackJackPlayer>,
    private val lister: RecyclerViewEvent
) : RecyclerView.Adapter<PlayerAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view){

        val nameTextView: TextView = view.findViewById(R.id.nameView)
        val moneyTextView: TextView = view.findViewById(R.id.moneyView)
        val editImageView: ImageView = view.findViewById(R.id.editImageView)
        val deleteImageView: ImageView = view.findViewById(R.id.deleteImageView)
       init {
                itemView.setOnClickListener{
               if (adapterPosition != RecyclerView.NO_POSITION) {
                   lister.onRowClick(adapterPosition)
               }
           }
       }

    }

    private fun deletePlayer(position: Int, name: String, context: Context){


        val dataManager = SaveDataManager(context)


        dataManager.deletePlayer(name)
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun confirmDialog( position: Int, context: Context){
        val name = data[position].name

        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Delete $name?")

        dialogBuilder.setPositiveButton("Delete") {dialog, _ ->
            deletePlayer(position, name, context)
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton("Cancel") {dialog, _ ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
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
        holder.moneyTextView.text = "$${player.money}"

        holder.deleteImageView.setOnClickListener{
            confirmDialog(position, it.context)
        }
        holder.editImageView.setOnClickListener{
            fragment.onEditClick(data[position].name)
        }
    }
}