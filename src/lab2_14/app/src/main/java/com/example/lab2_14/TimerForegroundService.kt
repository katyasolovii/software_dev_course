package com.example.lab2_14

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerForegroundService : Service() {

    private val serviceJob = Job()
    private val scope = CoroutineScope(Dispatchers.Default + serviceJob)
    private var isRunning = false

    // StateFlow для реактивної передачі секунд у UI
    private val _secondsFlow = MutableStateFlow(0)
    val secondsFlow: StateFlow<Int> = _secondsFlow.asStateFlow()

    // Binder, який повертає посилання на цей сервіс
    inner class LocalBinder : Binder() {
        fun getService(): TimerForegroundService = this@TimerForegroundService
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(1, notification)

        if (!isRunning) {
            isRunning = true
            startTimer()
        }

        return START_NOT_STICKY
    }

    private fun startTimer() {
        scope.launch {
            while (isRunning) {
                delay(1000)
                _secondsFlow.value += 1
                Log.d("Timer", "Минуло ${_secondsFlow.value} секунд")
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Фоновий Таймер",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Фоновий Таймер")
            .setContentText("Таймер відраховує час у фоні...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        _secondsFlow.value = 0
        scope.cancel()
        Log.d("Timer", "Службу зупинено, корутину скасовано")
    }

    companion object {
        private const val CHANNEL_ID = "timer_service_channel"
    }
}
