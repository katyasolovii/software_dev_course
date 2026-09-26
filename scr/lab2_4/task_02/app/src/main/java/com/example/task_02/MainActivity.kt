package com.example.task_02

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import timber.log.Timber
import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Timber.d("MainActivity: onCreate() викликано")
        findViewById<Button>(R.id.btnOpenSecond).setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.d("MainActivity: onStart() викликано")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("MainActivity: onResume() викликано")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("MainActivity: onPause() викликано")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("MainActivity: onStop() викликано")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("MainActivity: onDestroy() викликано")
    }
}