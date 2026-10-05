package com.example.task_01

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val audioPlayer = AudioPlayer()
    private val analyticsTracker = AnalyticsTracker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this).apply {
            text = "Натисніть сюди, щоб відкрити SecondActivity"
            textSize = 20f
            gravity = android.view.Gravity.CENTER
            setOnClickListener {
                startActivity(Intent(this@MainActivity, SecondActivity::class.java))
            }
        }
        setContentView(textView)

        lifecycle.addObserver(audioPlayer)
        lifecycle.addObserver(analyticsTracker)
    }
}