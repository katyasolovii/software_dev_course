package com.example.lab2_13

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class BroadcastRepository(private val context: Context) {

    // Функція для отримання потоку подій через callbackFlow
    fun getBroadcastEvents(): Flow<BroadcastEvent> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action ?: return

                // Форматування точного часу
                val timeFormat = SimpleDateFormat("HH:mm:ss (dd.MM.yyyy)", Locale.getDefault())
                val formattedTime = timeFormat.format(Date())

                // Формування деталей із додаткових даних (extras)
                val detailsBuilder = StringBuilder("Action: $action\n")
                intent.extras?.let { bundle ->
                    for (key in bundle.keySet()) {
                        detailsBuilder.append("Extra [$key]: ${bundle.get(key)}\n")
                    }
                }

                val eventType = when (action) {
                    Intent.ACTION_POWER_CONNECTED -> "Зарядний пристрій підключено"
                    Intent.ACTION_POWER_DISCONNECTED -> "Зарядний пристрій відключено"
                    Intent.ACTION_AIRPLANE_MODE_CHANGED -> "Режим 'У літаку' змінено"
                    Intent.ACTION_BATTERY_LOW -> "Критичний заряд батареї"
                    Intent.ACTION_HEADSET_PLUG -> "Гарнітура підключена/відключена"
                    else -> action
                }

                // Генерація об'єкта BroadcastEvent із точним часом
                val event = BroadcastEvent(
                    id = UUID.randomUUID().toString(),
                    type = eventType,
                    timestamp = formattedTime,
                    details = detailsBuilder.toString().trim()
                )

                // Надсилання події через trySend
                trySend(event)
            }
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_HEADSET_PLUG)
        }

        // Реєстрація приймача
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        // Обов'язкове скасування реєстрації приймача у блоці awaitClose
        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }
}