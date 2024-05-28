package com.marioloncar.data.tickers.data.mapper

import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.serialization.json.JsonArray

interface TickersRepositoryMapper {

    fun toTickers(tickersJson: JsonArray): List<Ticker>

    fun sanitizeSymbols(symbols: String?): String
}
