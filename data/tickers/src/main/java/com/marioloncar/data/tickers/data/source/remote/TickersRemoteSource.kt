package com.marioloncar.data.tickers.data.source.remote

import kotlinx.serialization.json.JsonArray

/**
 * Contract of Tickers remote source.
 */
interface TickersRemoteSource {

    /**
     * Fetches tickers.
     *
     * @return JSONArray representing tickers.
     */
    suspend fun fetchTickers(symbols: String = "ALL"): JsonArray
}
