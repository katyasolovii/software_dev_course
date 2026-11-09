package com.example.private24

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExchangeRateEntity::class], version = 1, exportSchema = false)
abstract class ExchangeDatabase : RoomDatabase() {

    abstract fun exchangeRateDao(): ExchangeRateDao

    companion object {
        @Volatile
        private var INSTANCE: ExchangeDatabase? = null

        fun getDatabase(context: Context): ExchangeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let { return it }

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExchangeDatabase::class.java,
                    "ExchangeRates.db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}