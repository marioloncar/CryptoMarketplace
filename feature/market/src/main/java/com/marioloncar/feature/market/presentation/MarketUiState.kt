package com.marioloncar.feature.market.presentation

import androidx.annotation.StringRes
import com.marioloncar.feature.market.R

/**
 * UI state definitions for Market screen.
 */
data class MarketUiState(
    @StringRes val title: Int = R.string.empty,
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

    data class TickerData(
        val name: String,
        val isEarnYield: Boolean,
        val bid: String,
        val dailyChange: String
    )
}
