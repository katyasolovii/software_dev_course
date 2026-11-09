package com.example.private24

sealed interface ExchangeArchiveUiState {
    // Початковий стан (до того, як користувач натиснув кнопку завантаження)
    object Initial : ExchangeArchiveUiState

    // Стан завантаження
    object Loading : ExchangeArchiveUiState

    // Успішно отримано відповідь
    data class Success(
        val requestedDate: String,
        val rates: List<ExchangeRateItem>
    ) : ExchangeArchiveUiState

    // Помилка мережі або сервера
    data class Error(val message: String) : ExchangeArchiveUiState
}