package com.marioloncar.feature.market.presentation.mapper

import com.marioloncar.data.tickers.domain.model.Ticker
import com.marioloncar.feature.market.presentation.MarketUiState

/**
 * Interface for mapping domain models to UI state models for the market screen.
 */
interface MarketUiStateMapper {

    /**
     * Maps a list of [Ticker] domain models to UI state model representing the tickers data.
     */
    fun toContent(tickers: List<Ticker>): MarketUiState.Tickers.Content

    /**
     * Maps an error to the UI state model representing the error.
     */
    fun toError(): MarketUiState.Tickers.Error

    /**
     * Maps a network connection error to the UI state model representing the network connection error.
     */
    fun toNetworkConnectionError(): MarketUiState.Tickers.Error
}
