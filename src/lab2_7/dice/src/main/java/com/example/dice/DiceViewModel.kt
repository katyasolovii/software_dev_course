package com.example.dice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.thread
import kotlin.random.Random

class DiceViewModel : ViewModel() {
    // Інкапсуляція LiveData
    private val _diceValues = MutableLiveData<List<Int>>(listOf(1, 1, 1, 1, 1))
    val diceValues: LiveData<List<Int>> = _diceValues

    private val _isRolling = MutableLiveData<Boolean>(false)
    val isRolling: LiveData<Boolean> = _isRolling

    private val currentDice = mutableListOf(1, 1, 1, 1, 1)

    // Список запущених фонових потоків
    private val threads = mutableListOf<Thread>()

    fun rollDice() {
        if (_isRolling.value == true) return
        _isRolling.value = true

        threads.clear()
        var finishedCount = 0

        // Кількість кроків по 100 мс для кожної з 5 кісток: 15=1.5с, 20=2.0с, 25=2.5с, 30=3.0с, 35=3.5с
        val stepsPerDie = listOf(15, 20, 25, 30, 35)
        for (i in 0..4) {
            val t = thread {
                try {
                    val steps = stepsPerDie[i]
                    for (step in 1..steps) {
                        Thread.sleep(100)
                        currentDice[i] = Random.nextInt(1, 7)
                        _diceValues.postValue(currentDice.toList())
                    }
                } catch (e: InterruptedException) {
                } finally {
                    finishedCount++
                    if (finishedCount == 5) {
                        _isRolling.postValue(false)
                    }
                }
            }
            threads.add(t)
        }
    }

    override fun onCleared() {
        super.onCleared()
        for (t in threads) {
            t.interrupt()
        }
    }
}