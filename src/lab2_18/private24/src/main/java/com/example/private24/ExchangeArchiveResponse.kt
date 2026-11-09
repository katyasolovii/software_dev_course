package com.example.private24

import com.google.gson.annotations.SerializedName

data class ExchangeArchiveResponse(
    val date: String = "",
    val bank: String = "",
    val baseCurrency: Int = 980,
    val baseCurrencyLit: String = "UAH",
    @SerializedName("exchangeRate")
    val exchangeRate: List<ExchangeRateItem>? = null
)