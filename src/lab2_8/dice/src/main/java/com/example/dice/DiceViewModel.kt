package com.example.dice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DiceViewModel : ViewModel() {
    private val diceModel = DiceModel()

    private val currentDice = mutableListOf(1, 1, 1, 1, 1)

    private val _diceValues = MutableLiveData<List<Int>>(listOf(1, 1, 1, 1, 1))
    val diceValues: LiveData<List<Int>> = _diceValues

    private val _buttonText = MutableLiveData<String>("ROLL")
    val buttonText: LiveData<String> = _buttonText

    private val _isRolling = MutableLiveData<Boolean>(false)
    val isRolling: LiveData<Boolean> = _isRolling

    private var rollJob: Job? = null

    // Оброблення кліку по кнопці
    fun onRollButtonClicked() {
        if (_isRolling.value == true) {
            rollJob?.cancel()
        }
        startRolling()
    }

    // Запуск корутини, міняє стан кнопки на "RESTART", одночасно крутить 5 кісток у паралельних корутинах (кожна зі своїм часом), чекає зупинки останньої і повертає стан кнопки на "ROLL"
    private fun startRolling() {
        // Запуск корутини у viewModelScope
        rollJob = viewModelScope.launch {
            _isRolling.value = true
            _buttonText.value = "RESTART"

            val stepsPerDie = listOf(15, 20, 25, 30, 35)
            val deferredList = mutableListOf<Deferred<Unit>>()

            coroutineScope {
                // Цикл для створення 5 паралельних корутин
                for (index in 0..4) {
                    val task = async {
                        val steps = stepsPerDie[index]

                        for (step in 1..steps) {
                            // Отримання нового випадкового значення
                            val newValue = diceModel.rollOnce()
                            currentDice[index] = newValue
                            _diceValues.value = currentDice.toList()
                        }
                    }
                    deferredList.add(task)
                }
                deferredList.awaitAll()
            }

            _isRolling.value = false
            _buttonText.value = "ROLL"
        }
    }
}