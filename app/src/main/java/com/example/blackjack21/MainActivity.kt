package com.example.blackjack21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    lateinit var fragmentContainer: FrameLayout
    lateinit var mainMenuFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentContainer = findViewById(R.id.fragmentContainer)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        GameManager.loadPlayers(this)

        mainMenuFragment = MainMenuFragment()

        fragmentTransaction.replace(R.id.fragmentContainer, mainMenuFragment)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, mainMenuFragment)
        fragmentTransaction.commit()
    }

}