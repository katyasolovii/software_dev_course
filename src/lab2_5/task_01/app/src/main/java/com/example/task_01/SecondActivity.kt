package com.example.task_01

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            text = "Це SecondActivity. Натисніть кнопку «Назад»!"
            textSize = 20f
            gravity = android.view.Gravity.CENTER
        })
    }
}