package com.marioloncar.data.tickers.domain.model

/**
 * Data class representing a ticker.
 *
 * @property pair The trading pair, e.g., "tBTCUSD".
 * @property bid The current bid price.
 * @property bidSize The size of the current bid.
 * @property ask The current ask price.
 * @property askSize The size of the current ask.
 * @property dailyChange The change in price over the last day.
 * @property dailyChangePerc The percentage change in price over the last day.
 * @property lastPrice The last traded price.
 * @property volume The trading volume over the last day.
 * @property high The highest price over the last day.
 * @property low The lowest price over the last day.
 */
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
