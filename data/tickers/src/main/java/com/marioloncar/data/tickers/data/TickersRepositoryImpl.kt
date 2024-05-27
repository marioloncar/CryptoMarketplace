package com.marioloncar.data.tickers.data

import com.marioloncar.data.tickers.data.source.remote.TickersRemoteSource
import com.marioloncar.data.tickers.domain.TickersRepository
import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive

internal class TickersRepositoryImpl(
    private val tickersRemoteSource: TickersRemoteSource,
) : TickersRepository {

    override fun getTickers(symbols: String?): Flow<List<Ticker>> = flow {
        val tickers = tickersRemoteSource.fetchTickers(symbols = sanitizeSymbols(symbols))

        val tickersList = tickers.map { data ->
            if (data is JsonArray) {
                Ticker(
                    pair = data[0].jsonPrimitive.content,
                    bid = data[1].jsonPrimitive.double,
                    bidSize = data[2].jsonPrimitive.double,
                    ask = data[3].jsonPrimitive.double,
                    askSize = data[4].jsonPrimitive.double,
                    dailyChange = data[5].jsonPrimitive.double,
                    dailyChangePerc = data[6].jsonPrimitive.double,
                    lastPrice = data[7].jsonPrimitive.double,
                    volume = data[8].jsonPrimitive.double,
                    high = data[9].jsonPrimitive.double,
                    low = data[10].jsonPrimitive.double
                )
            } else {
                throw IllegalArgumentException("Invalid data format")
            }
        }

        emit(tickersList)
    }

    private fun sanitizeSymbols(symbols: String?): String {
        if (symbols.isNullOrBlank()) return "ALL"

        return symbols.split(SPLIT_SYMBOLS_REGEX)
            .asSequence()
            .map(String::trim)
            .filter(String::isNotEmpty)
            .joinToString(",") { symbol ->
                val sanitizedSymbol = symbol
                    .replace(NON_ALPHA_NUMERIC_REGEX, "")
                    .uppercase()

                if (!sanitizedSymbol.startsWith("T")) {
                    "t$sanitizedSymbol"
                } else {
                    "t${sanitizedSymbol.substring(1)}"
                }
            }
    }

    private companion object {
        val SPLIT_SYMBOLS_REGEX = Regex("[,\\s]+")
        val NON_ALPHA_NUMERIC_REGEX = Regex("[^A-Za-z0-9]")
    }
}
