package com.example.private24

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExchangeArchiveViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ExchangeDatabase.getDatabase(application).exchangeRateDao()
    private val workManager = WorkManager.getInstance(application)

    private val _selectedDate = MutableStateFlow(
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
    )
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val ratesState: StateFlow<List<ExchangeRateEntity>> = _selectedDate
        .flatMapLatest { date ->
            dao.observeRates(date)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val workInfoState: StateFlow<WorkInfo.State?> = _selectedDate
        .flatMapLatest { date ->
            workManager.getWorkInfosForUniqueWorkFlow("exchange_rates_$date")
                .map { list -> list.firstOrNull()?.state }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    init {
        refreshRatesForSelectedDate()
    }

    fun onDateSelected(newDate: String) {
        _selectedDate.value = newDate
    }

    fun refreshRatesForSelectedDate() {
        val date = _selectedDate.value.trim()
        if (date.isBlank()) return

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeRequest = OneTimeWorkRequestBuilder<ExchangeRatesSyncWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf("date" to date))
            .build()

        workManager.enqueueUniqueWork(
            "exchange_rates_$date",
            ExistingWorkPolicy.REPLACE,
            oneTimeRequest
        )
    }
}