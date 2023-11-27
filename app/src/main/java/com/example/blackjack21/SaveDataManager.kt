package com.example.blackjack21

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val USER_DATA = "userDataMap"
class SaveDataManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("blackjack21", Context.MODE_PRIVATE)
    private val gson = Gson()


    // Updates a player returns true if players is found
    fun updatePlayer(playerToSave: BlackJackPlayer ): Boolean{
        val playerDataJson = sharedPreferences.getString(USER_DATA, null)
        var playerMap: MutableMap<String, BlackJackPlayer>? = null

        if (playerDataJson != null){
            val typeToken = object: TypeToken<MutableMap<String, BlackJackPlayer>>() {}.type
            playerMap = gson.fromJson(playerDataJson, typeToken)
            if (playerMap?.containsKey(playerToSave.name) == true){
                playerMap?.set(playerToSave.name, playerToSave)
                val newPlayerDataJson = gson.toJson(playerMap)
                sharedPreferences.edit().putString(USER_DATA, newPlayerDataJson).apply()
                return true
            }
        }
        return false
    }

    // Used to Save One New player with check for unique name
    fun saveNewPlayer(playerToSave: BlackJackPlayer): Boolean{
        val mapOfPlayers = loadPlayers()
        if (mapOfPlayers.containsKey(playerToSave.name)){
            return false
        }
        mapOfPlayers[playerToSave.name] = playerToSave
        val playersJson = gson.toJson(mapOfPlayers)
        sharedPreferences.edit().putString(USER_DATA, playersJson).apply()
        return true
    }


    fun savePlayers(playersToSave: MutableList<BlackJackPlayer>){
        val mapOfPlayers = loadPlayers()
        playersToSave.forEach{ player ->
            mapOfPlayers[player.name] = player
        }
        val playersJson = gson.toJson(mapOfPlayers)
        sharedPreferences.edit().putString(USER_DATA, playersJson).apply()
    }

    //Used to get a map of all players stored with the name as key
    fun loadPlayers(): MutableMap<String, BlackJackPlayer>{
        val usersDataJson = sharedPreferences.getString(USER_DATA, null)
        return if (usersDataJson != null){
            val typeToken = object: TypeToken<MutableMap<String, BlackJackPlayer>>() {}.type
            gson.fromJson(usersDataJson, typeToken)
        } else{
            return mutableMapOf()
        }
    }
    // Used to get a list of all players instead of a map.
    fun getListOfPlayers(): MutableList<BlackJackPlayer>{
        val mapOfPlayers = loadPlayers()
        val listOfPlayers: MutableList<BlackJackPlayer> = mapOfPlayers.values.toMutableList()
        return listOfPlayers
    }

    fun removeKey(keyToRemove: String){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(keyToRemove)
        editor.apply()
    }


}