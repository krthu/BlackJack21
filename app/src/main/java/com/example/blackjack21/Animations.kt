package com.example.blackjack21


import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet


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

    fun fadeInAndOut (view: View){
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 100
        fadeIn.fillAfter = true

        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.startOffset = 500
        fadeOut.duration = 500
        fadeOut.fillAfter = true

        val fadeInAndOut = AnimationSet(false)
        fadeInAndOut.addAnimation(fadeIn)
        fadeInAndOut.addAnimation(fadeOut)
        view.startAnimation(fadeInAndOut)
    }
}
