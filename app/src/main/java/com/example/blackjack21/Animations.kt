package com.example.blackjack21


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
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

    fun winAnimation(view: View){
        val yAnimation = ObjectAnimator.ofFloat(view, "translationY", 0f , view.height.toFloat()).apply {
            duration = 2000
        }
        val fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f ).apply {
            duration = 2000
        }
        val animationSet = AnimatorSet().apply{
            playTogether(yAnimation, fadeOut)
        }
        animationSet.start()

    }

    fun looseAnimation(view: View){
        val yAnimation = ObjectAnimator.ofFloat(view, "translationY", 0f , -view.height.toFloat()).apply {
            duration = 2000
        }
        val fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f ).apply {
            duration = 2000
        }
        val animationSet = AnimatorSet().apply{
            playTogether(yAnimation, fadeOut)
        }
        animationSet.start()

    }

    fun smallToBigAnimation(view: View){
        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f).apply {
            duration = 100
        }
        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f).apply {
            duration = 100
        }

        val fadeIn = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
            duration = 100
        }

        val animationSet = AnimatorSet().apply {
            playTogether(scaleX, scaleY, fadeIn)
        }
        animationSet.start()

    }

    fun fadeIn(view: View, duration: Long = 3000) {
        val fadeIn = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
            this.duration = duration
        }
        fadeIn.start()
    }

    fun fadeOut(view: View, duration: Long = 3000, endAction: () -> Unit = {}) {
        val fadeOut = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f).apply {
            this.duration = duration
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    endAction()
                }
            })
        }
        fadeOut.start()
    }
}
