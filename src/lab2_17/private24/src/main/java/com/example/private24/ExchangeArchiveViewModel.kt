package com.example.private24

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class ExchangeArchiveViewModel : ViewModel() {

    private val networkModule = PrivatBankNetworkModule()

    // Стан введеної дати (за замовчуванням сьогоднішня або популярна дата)
    private val _selectedDate = MutableStateFlow("01.11.2026")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    // Стан екрана
    private val _uiState = MutableStateFlow<ExchangeArchiveUiState>(ExchangeArchiveUiState.Initial)
    val uiState: StateFlow<ExchangeArchiveUiState> = _uiState.asStateFlow()

    // Зберігаємо поточний Call для його скасування за потреби
    private var currentCall: Call<ExchangeArchiveResponse>? = null

    fun onDateChanged(newDate: String) {
        _selectedDate.value = newDate
    }

    fun fetchArchiveRates() {
        val dateToFetch = _selectedDate.value.trim()

        // Перевірка формату та реальності дати (наприклад, відхилення 31.02.2026)
        if (!isValidDate(dateToFetch)) {
            _uiState.value = ExchangeArchiveUiState.Error("Введено некоректну дату. Використовуйте формат ДД.ММ.РРРР")
            return
        }

        // Скасовуємо попередній незавершений запит
        currentCall?.cancel()
        _uiState.value = ExchangeArchiveUiState.Loading

        val call = networkModule.apiService.getArchiveRates(dateToFetch)
        currentCall = call

        call.enqueue(object : Callback<ExchangeArchiveResponse> {
            override fun onResponse(
                call: Call<ExchangeArchiveResponse>,
                response: Response<ExchangeArchiveResponse>
            ) {
                // Ігноруємо результат, якщо запит скасовано або це застарілий виклик
                if (call !== currentCall || call.isCanceled) return

                if (!response.isSuccessful) {
                    _uiState.value = ExchangeArchiveUiState.Error(
                        "Помилка сервера: HTTP ${response.code()}. Спробуйте пізніше."
                    )
                    return
                }

                val body = response.body()
                if (body == null) {
                    _uiState.value = ExchangeArchiveUiState.Error("Сервер повернув порожню відповідь.")
                    return
                }

                // Відфільтровуємо записи без коду валюти (currency != null)
                val rawRates = body.exchangeRate.orEmpty()
                val validRates = rawRates.filter { !it.currency.isNull_or_blank() }

                _uiState.value = ExchangeArchiveUiState.Success(
                    requestedDate = body.date ?: dateToFetch,
                    rates = validRates
                )
            }

            override fun onFailure(call: Call<ExchangeArchiveResponse>, t: Throwable) {
                if (call !== currentCall || call.isCanceled) return

                _uiState.value = ExchangeArchiveUiState.Error(
                    "Не вдалося завантажити курси. Перевірте з'єднання та спробуйте знову."
                )
            }
        })
    }

    private fun isValidDate(dateStr: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            sdf.isLenient = false // Не дозволяє дати на зразок 31.02.2026
            sdf.parse(dateStr)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun String?.isNull_or_blank(): Boolean {
        return this == null || this.trim().isEmpty()
    }

    override fun onCleared() {
        currentCall?.cancel()
        super.onCleared()
    }
}