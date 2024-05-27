package com.marioloncar.feature.market.presentation.mapper

import com.marioloncar.data.tickers.domain.model.Ticker
import com.marioloncar.feature.market.R
import com.marioloncar.feature.market.presentation.MarketUiState
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

class MarketUiStateMapperImpl : MarketUiStateMapper {

    override fun toTitle(): Int = R.string.market

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
        return "$${BigDecimal(price).setScale(6, RoundingMode.HALF_UP).toPlainString()}"
    }

    override fun toError(): MarketUiState.Tickers.Error {
        return MarketUiState.Tickers.Error(R.string.unknown_error)
    }

    override fun toNetworkConnectionError(): MarketUiState.Tickers.Error {
        return MarketUiState.Tickers.Error(R.string.no_network_connection)
    }
}
