package com.marioloncar.data.tickers.data.source.remote

import com.marioloncar.core.network.BASE_URL
import com.marioloncar.core.network.BaseApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.JsonArray

class TickersRemoteSourceImpl(
    httpClient: HttpClient,
) : BaseApi(httpClient),
    TickersRemoteSource {

    override suspend fun fetchTickers(): JsonArray = execute {
        get("${BASE_URL}/tickers?symbols=ALL").body()
    }
}
