package com.example.blackjack21

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class saveDataManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("blackjack21", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveUsers(key: String, users: List<BlackJackPlayer>){
        val userDataJson = gson.toJson(users)
        sharedPreferences.edit().putString(key, userDataJson).apply()
    }

    fun loadUsers(key: String): MutableList<BlackJackPlayer>{
        val usersDataJson = sharedPreferences.getString(key, null)
        return if (usersDataJson != null){
            val typeToken = object: TypeToken<MutableList<BlackJackPlayer>>() {}.type
            gson.fromJson(usersDataJson, typeToken)
        } else{
            mutableListOf<BlackJackPlayer>()
        }

    }


}