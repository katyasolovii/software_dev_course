package com.example.task2_10

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch


class DownloadViewModel : ViewModel() {

    private val fileRepository = FileRepository()
    private var downloadJob: Job? = null

    // Інкапсульований StateFlow для відсотка та тексту статусу
    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress.asStateFlow()
    private val _statusText = MutableStateFlow("Очікування запуску")
    val statusText: StateFlow<String> = _statusText.asStateFlow()

    fun startDownload() {
        // Якщо завантаження вже активне, то повторний запуск заборонено
        if (downloadJob?.isActive == true) return
        downloadJob = viewModelScope.launch {
            fileRepository.downloadFile()
                // onCompletion - перевірка успіху або скасування
                .onCompletion { cause ->
                    if (cause == null) {
                        _statusText.value = "Файл успішно скачано!"
                    } else {
                        _statusText.value = "Завантаження скасовано"
                    }
                }
                .collect { progressValue ->
                    _progress.value = progressValue
                    _statusText.value = "Завантаження: $progressValue%"
                }
        }
    }
    fun cancelDownload() {
        downloadJob?.cancel()
    }
}
