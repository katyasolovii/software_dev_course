package com.example.private24

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExchangeRatesSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val inputDate = inputData.getString("date")
        val dateToFetch = if (!inputDate.isNullOrBlank()) {
            inputDate
        } else {
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        }

        val networkModule = PrivatBankNetworkModule()
        val database = ExchangeDatabase.getDatabase(applicationContext)

        return try {
            val response = networkModule.api.getArchiveRates(dateToFetch)

            if (!response.isSuccessful) {
                return Result.retry()
            }

            val body = response.body()
            if (body == null) {
                return Result.failure(workDataOf("error" to "Сервер повернув відповідь без даних."))
            }

            val rawRates = body.exchangeRate
            if (rawRates.isNullOrEmpty()) {
                return Result.failure(workDataOf("error" to "Сервер не повернув курсів"))
            }

            val responseDate = if (body.date.isNotBlank()) body.date else dateToFetch

            val entities = rawRates.mapNotNull { item ->
                val currencyCode = item.currency ?: return@mapNotNull null
                ExchangeRateEntity(
                    date = responseDate,
                    currency = currencyCode,
                    baseCurrency = item.baseCurrency ?: "UAH",
                    purchaseRate = item.purchaseRate,
                    saleRate = item.saleRate
                )
            }

            if (entities.isEmpty()) {
                return Result.failure(workDataOf("error" to "Сервер не повернув курсів"))
            }

            database.exchangeRateDao().insertRates(entities)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}