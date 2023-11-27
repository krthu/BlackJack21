package com.example.blackjack21

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val USER_DATA = "userData"
class SaveDataManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("blackjack21", Context.MODE_PRIVATE)
    private val gson = Gson()


    fun updatePlayer(playerToSave: BlackJackPlayer ): Boolean{
        val playerDataJson = sharedPreferences.getString(USER_DATA, null)
        var playerList: MutableList<BlackJackPlayer>? = null

        if (playerDataJson != null){
            val typeToken = object: TypeToken<MutableList<BlackJackPlayer>>() {}.type
            playerList = gson.fromJson(playerDataJson, typeToken)
            playerList?.forEachIndexed{ index, player ->
                if (player.name == playerToSave.name){
                    playerList[index] = playerToSave
                    val newPlayerDataJson = gson.toJson(playerList)
                    sharedPreferences.edit().putString(USER_DATA, newPlayerDataJson).apply()
                    return true
                }
            }
        }
        return false
    }

    fun saveNewPlayer(playerToSave: BlackJackPlayer): Boolean{
        val players = loadPlayers()
        players.forEach{player ->
            if (player.name == playerToSave.name){
                return false
            }
        }
        players.add(playerToSave)
        savePlayers(players)
        return true
    }


    fun savePlayers(users: List<BlackJackPlayer>){
        val userDataJson = gson.toJson(users)
        sharedPreferences.edit().putString(USER_DATA, userDataJson).apply()
    }

    fun loadPlayers(): MutableList<BlackJackPlayer>{
        val usersDataJson = sharedPreferences.getString(USER_DATA, null)
        return if (usersDataJson != null){
            val typeToken = object: TypeToken<MutableList<BlackJackPlayer>>() {}.type
            gson.fromJson(usersDataJson, typeToken)
        } else{
            mutableListOf<BlackJackPlayer>()
        }
    }

    fun removeKey(keyToRemove: String){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(keyToRemove)
        editor.apply()
    }


}