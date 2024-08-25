@file:Suppress("UndocumentedPublicClass")

package com.marioloncar.feature.market.presentation

import androidx.annotation.StringRes

/**
 * UI state definitions for Market screen.
 */
data class MarketUiState(
    val tickers: Tickers = Tickers.Loading,
) {

    /**
     * Mutually exclusive states for Tickers.
     */
    sealed interface Tickers {
        data class Content(val tickers: List<TickerData>) : Tickers
        data class Error(@StringRes val message: Int) : Tickers
        data object Loading : Tickers
    }

    /**
     * Represents data for a ticker entry.
     *
     * @property name The name of the ticker.
     * @property isEarnYield Indicates whether the ticker is earning yield.
     * @property bid The bid price for the ticker.
     * @property dailyChange The daily change in the ticker's value.
     */
    data class TickerData(
        val name: String,
        val isEarnYield: Boolean,
        val bid: String,
        val dailyChange: String
    )
}
