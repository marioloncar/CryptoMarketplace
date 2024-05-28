package com.marioloncar.data.tickers

import com.marioloncar.data.tickers.data.mapper.TickersRepositoryMapperImpl
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import org.junit.Assert.assertEquals
import org.junit.Test

class TickersRepositoryMapperTest {

    private val mapper = TickersRepositoryMapperImpl()

    @Test
    fun toTickers_with_valid_data() {
        val jsonString = """
            [
                ["tBTCUSD", 45000.0, 1.0, 45010.0, 1.5, 100.0, 0.0022, 45005.0, 1000.0, 46000.0, 44000.0],
                ["tETHUSD", 3000.0, 2.0, 3010.0, 2.5, 50.0, 0.017, 3005.0, 500.0, 3100.0, 2900.0]
            ]
        """.trimIndent()
        val jsonArray = Json.parseToJsonElement(jsonString).jsonArray

        val tickers = mapper.toTickers(jsonArray)

        assertEquals(2, tickers.size)

        val btcTicker = tickers[0]
        assertEquals("tBTCUSD", btcTicker.pair)
        assertEquals(45000.0, btcTicker.bid, 0.0)
        assertEquals(1.0, btcTicker.bidSize, 0.0)
        assertEquals(45010.0, btcTicker.ask, 0.0)
        assertEquals(1.5, btcTicker.askSize, 0.0)
        assertEquals(100.0, btcTicker.dailyChange, 0.0)
        assertEquals(0.0022, btcTicker.dailyChangePerc, 0.0)
        assertEquals(45005.0, btcTicker.lastPrice, 0.0)
        assertEquals(1000.0, btcTicker.volume, 0.0)
        assertEquals(46000.0, btcTicker.high, 0.0)
        assertEquals(44000.0, btcTicker.low, 0.0)

        val ethTicker = tickers[1]
        assertEquals("tETHUSD", ethTicker.pair)
        assertEquals(3000.0, ethTicker.bid, 0.0)
        assertEquals(2.0, ethTicker.bidSize, 0.0)
        assertEquals(3010.0, ethTicker.ask, 0.0)
        assertEquals(2.5, ethTicker.askSize, 0.0)
        assertEquals(50.0, ethTicker.dailyChange, 0.0)
        assertEquals(0.017, ethTicker.dailyChangePerc, 0.0)
        assertEquals(3005.0, ethTicker.lastPrice, 0.0)
        assertEquals(500.0, ethTicker.volume, 0.0)
        assertEquals(3100.0, ethTicker.high, 0.0)
        assertEquals(2900.0, ethTicker.low, 0.0)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun toTickers_with_invalid_data() {
        val jsonString = """
            [
                ["tBTCUSD", 45000.0, 1.0, 45010.0, 1.5, 100.0, 0.0022, 45005.0, 1000.0, 46000.0]
            ]
        """.trimIndent()
        val jsonArray = Json.parseToJsonElement(jsonString).jsonArray

        mapper.toTickers(jsonArray)
    }

    @Test
    fun test_sanitizeSymbols_with_null_or_blank_input() {
        assertEquals("ALL", mapper.sanitizeSymbols(null))
        assertEquals("ALL", mapper.sanitizeSymbols(""))
        assertEquals("ALL", mapper.sanitizeSymbols("   "))
    }

    @Test
    fun test_sanitizeSymbols_with_valid_input() {
        assertEquals("tBTCUSD,tETHUSD", mapper.sanitizeSymbols("BTCUSD, ETHUSD"))
        assertEquals("tBTCUSD,tETHUSD", mapper.sanitizeSymbols(" BTCUSD , ETHUSD "))
        assertEquals("tBTCUSD,tETHUSD", mapper.sanitizeSymbols("tBTCUSD,tETHUSD"))
    }

    @Test
    fun test_sanitizeSymbols_with_invalid_characters() {
        assertEquals("tBTCUSD,tETHUSD", mapper.sanitizeSymbols("BTC!@#USD, ETH%^&USD"))
    }

    @Test
    fun test_sanitizeSymbols_ensuring_t__prefix() {
        assertEquals("tBTCUSD,tETHUSD", mapper.sanitizeSymbols("BTCUSD, ETHUSD"))
        assertEquals("tABCDEF,tGHIJKL", mapper.sanitizeSymbols("ABCDEF, GHIJKL"))
    }
}
