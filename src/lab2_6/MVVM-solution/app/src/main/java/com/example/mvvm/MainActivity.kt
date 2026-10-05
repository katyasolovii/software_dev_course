package com.example.mvvm

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<TimerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Залишковий час
        viewModel.timerLiveData.observe(this) { seconds: Long ->
            if (seconds == 0L) {
                binding.tvTimer.text = "Timer is over!"
            } else {
                binding.tvTimer.text = "$seconds seconds left"
            }
        }

        // Блокування кнопки "Старт" і поля вводу під час роботи таймера
        viewModel.isTimerRunning.observe(this) { isRunning: Boolean ->
            binding.btnStart.isEnabled = !isRunning
            binding.etSecondsInput.isEnabled = !isRunning
        }

        // Кнопка "Старт" викликає функцію startTimer()
        binding.btnStart.setOnClickListener {
            startTimer()
        }

        // Кнопка "Скидання"
        binding.btnReset.setOnClickListener {
            binding.etSecondsInput.text.clear()
            viewModel.resetTimer()
        }
    }

    private fun startTimer() {
        val userSeconds = binding.etSecondsInput.text.toString().trim().toLongOrNull()
        viewModel.startTimer(userSeconds)
    }

    companion object {
        val TAG = "XXXX"
    }
}