package com.marioloncar.data.tickers.data.mapper

import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.serialization.json.JsonArray

/**
 * Contract for mappings on [TickersRepositoryMapper].
 */
interface TickersRepositoryMapper {

    /**
     * Maps JSON data representing ticker information to a list of [Ticker] objects.
     *
     * @param tickersJson JSON array containing ticker data.
     * @return A list of [Ticker] objects parsed from the JSON data.
     */
    fun toTickers(tickersJson: JsonArray): List<Ticker>

    /**
     * Sanitizes ticker symbols by removing non-alphanumeric characters and ensuring they start with 't'.
     *
     * @param symbols A string representing the ticker symbols to sanitize.
     * @return A sanitized string of ticker symbols, separated by commas.
     */
    fun sanitizeSymbols(symbols: String?): String
}
