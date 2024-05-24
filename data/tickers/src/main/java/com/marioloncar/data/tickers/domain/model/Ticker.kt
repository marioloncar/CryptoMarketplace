package com.marioloncar.data.tickers.domain.model

data class Ticker(
    val pair: String,              // "tBTCUSD"
    val bid: Double,               // 67134
    val bidSize: Double,           // 3.88925219
    val ask: Double,               // 67135
    val askSize: Double,           // 5.7323049
    val dailyChange: Double,       // -1203
    val dailyChangePerc: Double,   // -0.01759853
    val lastPrice: Double,         // 67155
    val volume: Double,            // 1078.69263145
    val high: Double,              // 68395
    val low: Double,                // 66419
)
