package com.example.dice

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.random.Random

class DiceModel {

    suspend fun rollOnce(): Int {
        delay(100)
        return Random.nextInt(1, 7)
    }

    suspend fun rollAllDiceAnimated(onStepCallback: (index: Int, value: Int) -> Unit) {
        coroutineScope {
            val deferredList = (0..4).map { index -> async {
                    val steps = Random.nextInt(15, 36)
                    for (step in 1..steps) {
                        val newValue = rollOnce()
                        onStepCallback(index, newValue)
                    }
                }
            }

            deferredList.awaitAll()
        }
    }
}
