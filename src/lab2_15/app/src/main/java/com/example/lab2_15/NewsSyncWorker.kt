package com.example.lab2_15

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class NewsSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val token = inputData.getString("user_token")
            Log.d("NewsSyncWorker", "Запуск синхронізації новин для токена: $token")

            delay(3000)
            Log.d("NewsSyncWorker", "Синхронізацію новин успішно завершено")

            val out = Data.Builder().putString("result", "Успіх!").build()
            Result.success(out)
        } catch (e: Exception) {
            Log.e("NewsSyncWorker", "Помилка під час завантаження новин", e)
            Result.failure()
        }
    }
}