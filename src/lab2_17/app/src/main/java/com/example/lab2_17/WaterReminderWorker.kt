package com.example.lab2_17

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class WaterReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // 1. Збільшуємо лічильник
        val prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val newCount = prefs.getInt(KEY_COUNT, 0) + 1
        prefs.edit().putInt(KEY_COUNT, newCount).apply()

        // 2. Показуємо сповіщення (якщо є дозвіл)
        showNotification()

        return Result.success()
    }

    private fun showNotification() {
        // Дозвіл на  Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }

        val manager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Канал потрібен з Android 8.0 (API 26)
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Нагадування про воду",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Трекер води")
            .setContentText("Час випити склянку води! 💧")
            .setSmallIcon(R.drawable.ic_water_drop)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        const val PREFS_NAME = "water_prefs"
        const val KEY_COUNT = "reminder_count"
        const val CHANNEL_ID = "water_channel"
    }
}