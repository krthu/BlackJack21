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

    fun saveNewPlayer(playerToSave: BlackJackPlayer): Boolean{
        val players = loadPlayers()


        if (players.containsKey(playerToSave.name)){
            return false
        }
        players.set(playerToSave.name, playerToSave)
        savePlayers(players)
        return true
    }


    fun savePlayers(players: MutableMap<String, BlackJackPlayer>){
        val userDataJson = gson.toJson(players)
        sharedPreferences.edit().putString(USER_DATA, userDataJson).apply()
    }

    fun loadPlayers(): MutableMap<String, BlackJackPlayer>{
        val usersDataJson = sharedPreferences.getString(USER_DATA, null)

        return if (usersDataJson != null){

            val typeToken = object: TypeToken<MutableMap<String, BlackJackPlayer>>() {}.type
            Log.d("!!!", "EmptyList")
            gson.fromJson(usersDataJson, typeToken)

        } else{
            return mutableMapOf()
        }
    }

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