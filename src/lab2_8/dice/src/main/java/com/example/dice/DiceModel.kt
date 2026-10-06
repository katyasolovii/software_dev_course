package com.example.dice

import kotlinx.coroutines.delay
import kotlin.random.Random

class DiceModel {
    suspend fun rollOnce(): Int {
        delay(100)
        return Random.nextInt(1, 7)
    }
}