package com.example.blackjack21

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SaveDataManager2(context: Context) {
//    private val sharedPreferences = context.getSharedPreferences(PREFERANCE_NAME, Context.MODE_PRIVATE)
//    private val gson = Gson()
//
//
//    // Updates a player returns true if players is found
//    fun updatePlayer(playerToSave: BlackJackPlayer ): Boolean{
//        val playerDataJson = sharedPreferences.getString(USER_DATA, null)
//        var playerMap: MutableMap<String, BlackJackPlayer>? = null
//
//        if (playerDataJson != null){
//            val typeToken = object: TypeToken<MutableMap<String, BlackJackPlayer>>() {}.type
//            playerMap = gson.fromJson(playerDataJson, typeToken)
//            if (playerMap?.containsKey(playerToSave.name.lowercase()) == true){
//                playerMap?.set(playerToSave.name.lowercase(), playerToSave)
//                val newPlayerDataJson = gson.toJson(playerMap)
//                sharedPreferences.edit().putString(USER_DATA, newPlayerDataJson).apply()
//                return true
//            }
//        }
//        return false
//    }
//
//    // Used to Save One New player with check for unique name
//    fun saveNewPlayer(playerToSave: BlackJackPlayer): Boolean{
//        val mapOfPlayers = loadPlayers()
//        if (mapOfPlayers.containsKey(playerToSave.name.lowercase())){
//            return false
//        }
//        mapOfPlayers[playerToSave.name.lowercase()] = playerToSave
//        val playersJson = gson.toJson(mapOfPlayers)
//        sharedPreferences.edit().putString(USER_DATA, playersJson).apply()
//        return true
//    }
//
//
//    fun savePlayers(playersToSave: MutableList<BlackJackPlayer>){
//        val mapOfPlayers = loadPlayers()
//        playersToSave.forEach{ player ->
//            mapOfPlayers[player.name.lowercase()] = player
//        }
//        val playersJson = gson.toJson(mapOfPlayers)
//        sharedPreferences.edit().putString(USER_DATA, playersJson).apply()
//    }
//
//    //Used to get a map of all players stored with the name as key
//    private fun loadPlayers(): MutableMap<String, BlackJackPlayer>{
//        val usersDataJson = sharedPreferences.getString(USER_DATA, null)
//        return if (usersDataJson != null){
//            val typeToken = object: TypeToken<MutableMap<String, BlackJackPlayer>>() {}.type
//            gson.fromJson(usersDataJson, typeToken)
//        } else{
//            return mutableMapOf()
//        }
//    }
//
//    // Used to get a list of all players instead of a map.
//    fun getListOfPlayers(): MutableList<BlackJackPlayer> {
//        val mapOfPlayers = loadPlayers()
//
//        return mapOfPlayers.values.toMutableList()
//    }
//
//    fun deletePlayer(name: String): Boolean{
//        val mapOfPlayers = loadPlayers()
//        if (mapOfPlayers.remove(name.lowercase()) != null) {
//
//            val playersJson = gson.toJson(mapOfPlayers)
//            sharedPreferences.edit().putString(USER_DATA, playersJson).apply()
//            return true
//        }
//        return false
//    }
//
//    fun updatePlayerName(newName: String, oldName: String): Boolean {
//        val mapOfPlayers = loadPlayers()
//        var playerToUpdate = mapOfPlayers.remove(oldName.lowercase())
//        if (playerToUpdate != null) {
//            playerToUpdate.name = newName
//            mapOfPlayers[playerToUpdate.name.lowercase()] = playerToUpdate
//            val playersJson = gson.toJson(mapOfPlayers)
//            sharedPreferences.edit().putString(USER_DATA, playersJson).apply()
//            return true
//        }
//        return false
//    }
//
//    // Deletes all user data
//    fun removeKey(){
//        val editor: SharedPreferences.Editor = sharedPreferences.edit()
//        editor.remove(USER_DATA)
//        editor.apply()
//    }
}