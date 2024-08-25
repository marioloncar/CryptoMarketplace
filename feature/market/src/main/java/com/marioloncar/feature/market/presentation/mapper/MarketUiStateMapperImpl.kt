package com.marioloncar.feature.market.presentation.mapper

import com.marioloncar.data.tickers.domain.model.Ticker
import com.marioloncar.feature.market.R
import com.marioloncar.feature.market.presentation.MarketUiState
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

private const val FORMAT_PRICE_SCALE = 6

/**
 * Implementation of [MarketUiStateMapper] responsible for mapping domain models to UI state models
 * for the market screen.
 */
class MarketUiStateMapperImpl : MarketUiStateMapper {

    /**
     * Maps a list of [Ticker] domain models to [MarketUiState.Tickers.Content] UI state model.
     *
     * @param tickers The list of ticker domain models.
     * @return The [MarketUiState.Tickers.Content] UI state model representing the tickers data.
     */
    override fun toContent(tickers: List<Ticker>): MarketUiState.Tickers.Content {
        return MarketUiState.Tickers.Content(
            tickers.map { ticker ->
                MarketUiState.TickerData(
                    name = ticker.pair,
                    isEarnYield = ticker.dailyChange > 0,
                    bid = formatPrice(ticker.bid),
                    dailyChange = formatDailyChange(dailyChange = ticker.dailyChange)
                )
            }
        )
    }

    private fun formatDailyChange(dailyChange: Double): String {
        return String.format(Locale.US, "%.2f%%", dailyChange * 100, Locale.US)
    }

    private fun formatPrice(price: Double): String {
        return "$${BigDecimal(price).setScale(FORMAT_PRICE_SCALE, RoundingMode.HALF_UP).toPlainString()}"
    }

    /**
     * Maps an error to the [MarketUiState.Tickers.Error] UI state model.
     *
     * @return The [MarketUiState.Tickers.Error] UI state model representing the error.
     */
    override fun toError(): MarketUiState.Tickers.Error {
        return MarketUiState.Tickers.Error(R.string.unknown_error)
    }

    /**
     * Maps a network connection error to the [MarketUiState.Tickers.Error] UI state model.
     *
     * @return The [MarketUiState.Tickers.Error] UI state model representing the network connection error.
     */
    override fun toNetworkConnectionError(): MarketUiState.Tickers.Error {
        return MarketUiState.Tickers.Error(R.string.no_network_connection)
    }
}
