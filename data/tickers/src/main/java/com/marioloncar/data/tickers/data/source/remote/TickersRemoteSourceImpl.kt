package com.marioloncar.data.tickers.data.source.remote

import com.marioloncar.core.network.BASE_URL
import com.marioloncar.core.network.BaseApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TickersRemoteSourceImpl(
    httpClient: HttpClient,
) : BaseApi(httpClient),
    TickersRemoteSource {

    override suspend fun fetchTickers(): List<List<Any>> = execute {
        get("${BASE_URL}/tickers").body()
    }
}
