package com.example.lab2_13

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return

        when (action) {
            Intent.ACTION_POWER_CONNECTED -> {
                Log.d("MyBroadcastReceiver", "Зарядний пристрій підключено")
                Toast.makeText(context, "Зарядний пристрій підключено", Toast.LENGTH_SHORT).show()
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                Log.d("MyBroadcastReceiver", "Зарядний пристрій відключено")
                Toast.makeText(context, "Зарядний пристрій відключено", Toast.LENGTH_SHORT).show()
            }
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                val isAirplaneModeOn = intent.getBooleanExtra("state", false)
                Log.d("MyBroadcastReceiver", "Режим 'У літаку' змінено: $isAirplaneModeOn")
                Toast.makeText(context, "Режим 'У літаку': $isAirplaneModeOn", Toast.LENGTH_SHORT).show()
            }
            Intent.ACTION_BATTERY_LOW -> {
                Log.d("MyBroadcastReceiver", "Низький рівень заряду батареї")
                Toast.makeText(context, "Низький рівень заряду батареї!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}