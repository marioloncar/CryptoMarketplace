package com.marioloncar.data.tickers.data.source.remote

import kotlinx.serialization.json.JsonArray

interface TickersRemoteSource {

    suspend fun fetchTickers(symbols: String = "ALL"): JsonArray
}
