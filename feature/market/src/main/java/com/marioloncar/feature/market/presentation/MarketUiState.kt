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
        data class Content(val tickers: List<String>) : Tickers
        data class Error(val message: String) : Tickers
        data object Loading : Tickers
    }
}
