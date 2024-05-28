package com.marioloncar.feature.market

import com.marioloncar.data.tickers.domain.model.Ticker
import com.marioloncar.feature.market.presentation.MarketUiState
import com.marioloncar.feature.market.presentation.mapper.MarketUiStateMapperImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class MarketUiStateMapperImplTest {

    private val mapper = MarketUiStateMapperImpl()

    @Test
    fun test_toTitle() {
        val titleResId = mapper.toTitle()
        assertEquals(R.string.market, titleResId)
    }

    @Test
    fun test_toContent_with_positive_daily_change() {
        val tickers = listOf(
            Ticker(
                pair = "tBTCUSD",
                bid = 45000.0,
                bidSize = 1.0,
                ask = 45010.0,
                askSize = 1.5,
                dailyChange = 100.0,
                dailyChangePerc = 0.0022,
                lastPrice = 45005.0,
                volume = 1000.0,
                high = 46000.0,
                low = 44000.0
            ),
            Ticker(
                pair = "tETHUSD",
                bid = 3000.0,
                bidSize = 2.0,
                ask = 3010.0,
                askSize = 2.5,
                dailyChange = 50.0,
                dailyChangePerc = 0.017,
                lastPrice = 3005.0,
                volume = 500.0,
                high = 3100.0,
                low = 2900.0
            )
        )

        val uiState = mapper.toContent(tickers)

        val expected = MarketUiState.Tickers.Content(
            listOf(
                MarketUiState.TickerData(
                    name = "tBTCUSD",
                    isEarnYield = true,
                    bid = "$45000.000000",
                    dailyChange = "10000.00%"
                ),
                MarketUiState.TickerData(
                    name = "tETHUSD",
                    isEarnYield = true,
                    bid = "$3000.000000",
                    dailyChange = "5000.00%"
                )
            )
        )

        assertEquals(expected, uiState)
    }

    @Test
    fun test_toContent_with_negative_daily_change() {
        val tickers = listOf(
            Ticker(
                pair = "tBTCUSD",
                bid = 45000.0,
                bidSize = 1.0,
                ask = 45010.0,
                askSize = 1.5,
                dailyChange = -100.0,
                dailyChangePerc = -0.0022,
                lastPrice = 45005.0,
                volume = 1000.0,
                high = 46000.0,
                low = 44000.0
            )
        )

        val uiState = mapper.toContent(tickers)

        val expected = MarketUiState.Tickers.Content(
            listOf(
                MarketUiState.TickerData(
                    name = "tBTCUSD",
                    isEarnYield = false,
                    bid = "$45000.000000",
                    dailyChange = "-10000.00%"
                )
            )
        )

        assertEquals(expected, uiState)
    }

    @Test
    fun test_formatDailyChange() {
        val positiveChange = 0.0022
        val formattedPositiveChange = mapper.toContent(
            listOf(
                Ticker(
                    pair = "tBTCUSD",
                    bid = 45000.0,
                    bidSize = 1.0,
                    ask = 45010.0,
                    askSize = 1.5,
                    dailyChange = 0.22,
                    dailyChangePerc = positiveChange,
                    lastPrice = 45005.0,
                    volume = 1000.0,
                    high = 46000.0,
                    low = 44000.0
                )
            )
        ).tickers[0].dailyChange

        assertEquals("22.00%", formattedPositiveChange)
    }

    @Test
    fun test_formatPrice() {
        val price = 45000.123456
        val formattedPrice = mapper.toContent(
            listOf(
                Ticker(
                    pair = "tBTCUSD",
                    bid = price,
                    bidSize = 1.0,
                    ask = 45010.0,
                    askSize = 1.5,
                    dailyChange = 100.0,
                    dailyChangePerc = 0.0022,
                    lastPrice = 45005.0,
                    volume = 1000.0,
                    high = 46000.0,
                    low = 44000.0
                )
            )
        ).tickers[0].bid

        assertEquals("$45000.123456", formattedPrice)
    }

    @Test
    fun test_toError() {
        val errorState = mapper.toError()
        assertEquals(R.string.unknown_error, errorState.message)
    }

    @Test
    fun test_noNetworkConnectionError() {
        val networkErrorState = mapper.toNetworkConnectionError()
        assertEquals(R.string.no_network_connection, networkErrorState.message)
    }
}
