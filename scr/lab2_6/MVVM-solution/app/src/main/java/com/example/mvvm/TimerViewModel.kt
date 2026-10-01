package com.example.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm.MainActivity.Companion.TAG

class TimerViewModel : ViewModel() {

    // Шар Model
    private val timerModel = TimerModel()

    init {
        Log.d(TAG, "TimerViewModel created: ")
    }

    private val _timerLiveData = MutableLiveData<Long>(30L)
    val timerLiveData: LiveData<Long>
        get() = _timerLiveData

    private val _isTimerRunning = MutableLiveData<Boolean>(false)
    val isTimerRunning: LiveData<Boolean>
        get() = _isTimerRunning

    // Запуск таймера із заданим або стандартним часом (30 сек)
    fun startTimer(userSeconds: Long? = null) {
        Log.d(TAG, "Timer starts")
        val seconds = if (userSeconds != null && userSeconds > 0) userSeconds else 30L

        _timerLiveData.value = seconds
        _isTimerRunning.value = true

        timerModel.start(
            seconds = seconds,
            onTick = { secondsLeft ->
                Log.d(TAG, "onTick: $secondsLeft")
                _timerLiveData.value = secondsLeft
            },
            onFinish = {
                Log.d(TAG, "onFinish: Timer is over!")
                _timerLiveData.value = 0L
                _isTimerRunning.value = false
            }
        )
    }

    fun resetTimer(resetSeconds: Long = 30L) {
        timerModel.stop()
        _isTimerRunning.value = false
        _timerLiveData.value = resetSeconds
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "TimerViewModel Cleared: ")
        timerModel.stop()
    }
}