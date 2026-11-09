package com.example.private24

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PrivatBankNetworkModule {

    private val baseUrl = "https://api.privatbank.ua/p24api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: PrivatBankApi = retrofit.create(PrivatBankApi::class.java)
}