package com.marioloncar.data.tickers.data.source.remote

import com.marioloncar.core.network.BaseApi
import io.ktor.client.HttpClient

class TickersRemoteSourceImpl(
    httpClient: HttpClient,
) : BaseApi(httpClient),
    TickersRemoteSource {

    override suspend fun fetchTickers(): List<String> = execute {
        TODO("Not yet implemented")
    }
}
