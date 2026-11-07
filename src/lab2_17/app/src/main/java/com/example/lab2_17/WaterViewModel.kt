package com.example.lab2_17

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class WaterViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences(
        WaterReminderWorker.PREFS_NAME, Context.MODE_PRIVATE
    )

    private val workManager = WorkManager.getInstance(application)

    private val _isEnabled = MutableStateFlow(prefs.getBoolean(KEY_ENABLED, false))
    val isEnabled: StateFlow<Boolean> = _isEnabled

    private val _count = MutableStateFlow(prefs.getInt(WaterReminderWorker.KEY_COUNT, 0))
    val count: StateFlow<Int> = _count

    fun startReminder(intervalMinutes: Long) {
        // Мінімальний системний інтервал для PeriodicWorkRequest — 15 хвилин
        val safeInterval = if (intervalMinutes < 15) 15L else intervalMinutes

        val request = PeriodicWorkRequestBuilder<WaterReminderWorker>(
            safeInterval, TimeUnit.MINUTES
        ).build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )

        prefs.edit().putBoolean(KEY_ENABLED, true).apply()
        _isEnabled.value = true
    }

    fun stopReminder() {
        workManager.cancelUniqueWork(WORK_NAME)
        prefs.edit().putBoolean(KEY_ENABLED, false).apply()
        _isEnabled.value = false
    }

    fun refreshCount() {
        _count.value = prefs.getInt(WaterReminderWorker.KEY_COUNT, 0)
    }

    companion object {
        const val WORK_NAME = "water_reminder"
        private const val KEY_ENABLED = "reminder_enabled"
    }
}