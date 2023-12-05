package com.example.blackjack21

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
const val USER_DATA = "userDataMap"
const val PREFERENCE_NAME  = "blackjack21"
object GameManager {
    var players: MutableMap<String, BlackJackPlayer> = mutableMapOf()
    var activePlayer: BlackJackPlayer? = null
    val gson = Gson()
    var currentLanguage = "en"



    fun loadPlayers(context: Context){
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val usersDataJson = sharedPreferences.getString(USER_DATA, null)
        if (usersDataJson != null){
            val typeToken = object: TypeToken<MutableMap<String, BlackJackPlayer>>() {}.type
            players = gson.fromJson(usersDataJson, typeToken)
        }
    }

    fun savePlayersToSharedPref(context: Context){
        val playersJson = gson.toJson(players)
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(USER_DATA, playersJson).apply()
    }

    fun saveNewPlayer(playerToSave: BlackJackPlayer, context: Context):Boolean{
       if (players.containsKey(playerToSave.name.lowercase().trim()))
        {
            return false
        }
        players[playerToSave.name.lowercase().trim()] = playerToSave
        savePlayersToSharedPref(context)
        return true
    }

    fun updatePlayerName(newName: String, oldName: String, context: Context): Boolean{
        if (!players.containsKey(newName.lowercase().trim()))
        {
            val playerToUpdate = players.remove(oldName.lowercase())
            if (playerToUpdate != null){
                playerToUpdate.name = newName
                players[playerToUpdate.name.lowercase().trim()] = playerToUpdate
                savePlayersToSharedPref(context)
                return true
            }
        }
        return false
    }

    fun deletePlayer(name: String, context: Context){
        players.remove(name.lowercase())
        savePlayersToSharedPref(context)
    }

    fun setActivePlayer(playerName: String){
        activePlayer = players[playerName.lowercase()]
    }

    fun removeKey(context: Context){
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(USER_DATA)
        editor.apply()
    }


}