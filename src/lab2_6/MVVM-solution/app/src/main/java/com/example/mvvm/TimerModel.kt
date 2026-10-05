package com.example.mvvm

import android.os.CountDownTimer

class TimerModel {

    private var timer: CountDownTimer? = null

    fun start(seconds: Long, onTick: (Long) -> Unit, onFinish: () -> Unit) {
        stop()

        timer = object : CountDownTimer(seconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000L
                onTick(secondsLeft)
            }

            override fun onFinish() {
                onFinish()
            }
        }.start()
    }

    fun stop() {
        timer?.cancel()
        timer = null
    }
}