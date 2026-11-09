package com.example.private24

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<ExchangeRateEntity>)

    @Query("SELECT * FROM exchange_rates WHERE date = :date ORDER BY currency ASC")
    fun observeRates(date: String): Flow<List<ExchangeRateEntity>>
}