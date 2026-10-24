package com.example.lab2_13

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BroadcastViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BroadcastRepository(application.applicationContext)

    // Змінний список отриманих подій
    private val _events = MutableStateFlow<List<BroadcastEvent>>(emptyList())
    val events: StateFlow<List<BroadcastEvent>> = _events.asStateFlow()

    init {
        // Підписка на потік подій у viewModelScope
        viewModelScope.launch {
            repository.getBroadcastEvents().collect { newEvent ->
                // Додаємо кожну нову подію на початок списку (згори)
                _events.value = listOf(newEvent) + _events.value
            }
        }
    }

    // Метод для вилучення обраного запису зі списку
    fun deleteEvent(event: BroadcastEvent) {
        _events.value = _events.value.filter { it.id != event.id }
    }

    // Додатковий метод для очищення всього списку
    fun clearAll() {
        _events.value = emptyList()
    }
}