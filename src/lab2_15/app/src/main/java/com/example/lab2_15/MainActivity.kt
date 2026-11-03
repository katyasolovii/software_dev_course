package com.example.lab2_15

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.lab2_15.ui.theme.Lab2_15Theme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab2_15Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NewsSyncScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NewsSyncScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val workManager = remember { WorkManager.getInstance(context) }

    // Статус виконання одноразової задачі
    var syncStatusText by remember { mutableStateOf("Очікує...") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Синхронізація новин",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Статус: $syncStatusText",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Одноразова задача (Кнопка "Синхронізувати зараз")
        Button(
            onClick = {
                // Умова запуску - наявність будь-якого інтернету
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                // OneTimeWorkRequestBuilder з токеном "abc123" та constraints
                val inputData = Data.Builder()
                    .putString("user_token", "abc123")
                    .build()

                val oneTimeRequest = OneTimeWorkRequestBuilder<NewsSyncWorker>()
                    .setConstraints(constraints)
                    .setInputData(inputData)
                    .build()

                // Задача в черзі WorkManager
                workManager.enqueue(oneTimeRequest)

                // Отримання статусу виконання через LiveData
                workManager.getWorkInfoByIdLiveData(oneTimeRequest.id)
                    .observe(lifecycleOwner) { workInfo ->
                        if (workInfo != null) {
                            when (workInfo.state) {
                                WorkInfo.State.ENQUEUED -> syncStatusText = "У черзі"
                                WorkInfo.State.RUNNING -> syncStatusText = "Виконується"
                                WorkInfo.State.SUCCEEDED -> {
                                    val message = workInfo.outputData.getString("result")
                                    syncStatusText = message ?: "Успішно завантажено"
                                }
                                WorkInfo.State.FAILED -> syncStatusText = "Помилка"
                                WorkInfo.State.CANCELLED -> syncStatusText = "Скасовано"
                                else -> syncStatusText = workInfo.state.name
                            }
                        }
                    }
            }
        ) {
            Text("Синхронізувати зараз")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Періодична задача (Кнопка "Увімкнути нічну синхронізацію")
        Button(
            onClick = {
                // Умови - Wi-Fi (UNMETERED) та Зарядка (RequiresCharging)
                val nightConstraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    .setRequiresCharging(true)
                    .build()

                // PeriodicWorkRequestBuilder на 15 хвилин
                val periodicRequest = PeriodicWorkRequestBuilder<NewsSyncWorker>(
                    15, TimeUnit.MINUTES
                )
                    .setConstraints(nightConstraints)
                    .setInputData(
                        Data.Builder().putString("user_token", "abc123").build()
                    )
                    .build()

                // Унікальна періодична задача із ім'ям "night_news_sync" та KEEP
                workManager.enqueueUniquePeriodicWork(
                    "night_news_sync",
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicRequest
                )

                syncStatusText = "Нічну синхронізацію увімкнено (Wi-Fi + Зарядка)"
            }
        ) {
            Text("Увімкнути нічну синхронізацію")
        }
    }
}