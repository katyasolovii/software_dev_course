package com.example.task_03

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserTracker.register(this)
        Timber.d("UserTracker: Зареєстровано слухачів у UserTracker: ${UserTracker.getListenersCount()}")
        val textView = TextView(this).apply {
            text = "Повертайте екран!"
            textSize = 22f
            gravity = android.view.Gravity.CENTER
        }
        setContentView(textView)
    }
}