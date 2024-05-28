package com.marioloncar.data.tickers.data.source.remote

import com.marioloncar.core.network.BASE_URL
import com.marioloncar.core.network.BaseApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.JsonArray

/**
 * Implementation of [TickersRemoteSource] responsible for fetching ticker data from a remote data source.
 *
 * @param httpClient The HTTP client used to make requests.
 */
class TickersRemoteSourceImpl(
    httpClient: HttpClient,
) : BaseApi(httpClient),
    TickersRemoteSource {

    /**
     * Fetches ticker data from the remote data source.
     *
     * @param symbols A string representing the ticker symbols to fetch data for.
     * @return A JSON array containing ticker data.
     * @throws IOException if there is an error fetching data from the remote source.
     */
    override suspend fun fetchTickers(symbols: String): JsonArray = execute {
        get("${BASE_URL}/tickers?symbols=$symbols").body()
    }
}
