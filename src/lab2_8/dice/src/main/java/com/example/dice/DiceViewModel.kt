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

    private fun startRolling() {
        rollJob = viewModelScope.launch {
            _isRolling.value = true
            _buttonText.value = "RESTART"
            // Виклик з DiceModel
            diceModel.rollAllDiceAnimated { index, newValue ->
                synchronized(currentDice) {
                    currentDice[index] = newValue
                    _diceValues.postValue(currentDice.toList())
                }
            }
            _isRolling.value = false
            _buttonText.value = "ROLL"
        }
    }
}
