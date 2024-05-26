package com.marioloncar.feature.market.presentation

/**
 * UI state definitions for Market screen.
 */
data class MarketUiState(
    val title: String = "",
    val tickers: Tickers = Tickers.Loading,
) {

    /**
     * Mutually exclusive states for Tickers.
     */
    sealed interface Tickers {
        data class Content(val tickers: List<TickerData>) : Tickers
        data class Error(val message: String) : Tickers
        data object Loading : Tickers
    }

    data class TickerData(
        val name: String,
        val isEarnYield: Boolean,
        val bid: String,
        val dailyChange: String
    )
}
