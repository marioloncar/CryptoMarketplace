package com.marioloncar.feature.market.presentation.mapper

import androidx.annotation.StringRes
import com.marioloncar.data.tickers.domain.model.Ticker
import com.marioloncar.feature.market.presentation.MarketUiState

interface MarketUiStateMapper {

    @StringRes
    fun toTitle(): Int

    fun toContent(tickers: List<Ticker>): MarketUiState.Tickers.Content

    fun toError(): MarketUiState.Tickers.Error

    fun toNetworkConnectionError(): MarketUiState.Tickers.Error
}
