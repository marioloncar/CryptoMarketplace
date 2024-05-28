package com.marioloncar.data.tickers.data.mapper

import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive

/**
 * Implementation of [TickersRepositoryMapper].
 */
class TickersRepositoryMapperImpl : TickersRepositoryMapper {

    /**
     * Maps JSON data representing ticker information to a list of [Ticker] objects.
     *
     * @param tickersJson JSON array containing ticker data.
     * @return A list of [Ticker] objects parsed from the JSON data.
     * @throws IllegalArgumentException if the JSON data does not match the expected format.
     */
    override fun toTickers(tickersJson: JsonArray): List<Ticker> {
        return tickersJson.map { data ->
            require(data is JsonArray) { "Invalid data format: Each item must be a JsonArray" }
            require(data.size == TICKERS_DATA_ELEMENT_COUNT) {
                "Invalid data format: Each JsonArray must contain 11 elements"
            }

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
        }
    }

    /**
     * Sanitizes ticker symbols by removing non-alphanumeric characters and ensuring they start with 't'.
     *
     * @param symbols A string representing the ticker symbols to sanitize.
     * @return A sanitized string of ticker symbols, separated by commas.
     */
    override fun sanitizeSymbols(symbols: String?): String {
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
        const val TICKERS_DATA_ELEMENT_COUNT = 11
    }
}
