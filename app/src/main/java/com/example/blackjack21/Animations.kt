package com.example.blackjack21


import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View


object Animations {


    fun pulse(view: View, duration: Long = 1000) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.5f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.5f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            this.duration = duration
        }
        animator.start()
    }

    fun pulse2(view: View, duration: Long = 1200) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.1f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            this.duration = duration
        }
        animator.start()
    }
}
