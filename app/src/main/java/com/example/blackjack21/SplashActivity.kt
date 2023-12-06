package com.example.blackjack21

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashLogo = findViewById<View>(R.id.splash_logo)
        Animations.fadeIn(splashLogo, 3000)

        Handler().postDelayed({
            Animations.fadeOut(splashLogo, 3000) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 3000)
    }
}

