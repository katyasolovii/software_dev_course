package com.example.lab2_17

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.lab2_17.ui.theme.Lab2_17Theme
import androidx.compose.foundation.layout.padding

class MainActivity : ComponentActivity() {

    private val viewModel: WaterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab2_17Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WaterReminderScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    // Коли повертаємось у застосунок, оновлюємо лічильник, який змінив Worker
    override fun onResume() {
        super.onResume()
        viewModel.refreshCount()
    }
}