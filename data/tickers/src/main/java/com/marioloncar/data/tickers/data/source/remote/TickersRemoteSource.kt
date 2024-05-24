package com.marioloncar.data.tickers.data.source.remote

interface TickersRemoteSource {

    suspend fun fetchTickers(): List<String>
}
