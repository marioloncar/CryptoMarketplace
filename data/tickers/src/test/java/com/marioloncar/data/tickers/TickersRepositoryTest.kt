package com.marioloncar.data.tickers

import com.marioloncar.data.tickers.data.TickersRepositoryImpl
import com.marioloncar.data.tickers.data.mapper.TickersRepositoryMapper
import com.marioloncar.data.tickers.data.source.remote.TickersRemoteSource
import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TickersRepositoryImplTest {

    @Mock
    private lateinit var tickersRemoteSource: TickersRemoteSource

    @Mock
    private lateinit var tickersRepositoryMapper: TickersRepositoryMapper

    private lateinit var tickersRepository: TickersRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        tickersRepository = TickersRepositoryImpl(tickersRemoteSource, tickersRepositoryMapper)
    }

    @Test
    fun getTickers_successfully_fetches_and_maps_tickers() = runTest {
        val symbols = "BTCUSD, ETHUSD"
        val sanitizedSymbols = "tBTCUSD,tETHUSD"
        val jsonArray = JsonArray(listOf(
            JsonArray(listOf(
                JsonPrimitive("tBTCUSD"), JsonPrimitive(45000.0), JsonPrimitive(1.0),
                JsonPrimitive(45010.0), JsonPrimitive(1.5), JsonPrimitive(100.0),
                JsonPrimitive(0.0022), JsonPrimitive(45005.0), JsonPrimitive(1000.0),
                JsonPrimitive(46000.0), JsonPrimitive(44000.0)
            )),
            JsonArray(listOf(
                JsonPrimitive("tETHUSD"), JsonPrimitive(3000.0), JsonPrimitive(2.0),
                JsonPrimitive(3010.0), JsonPrimitive(2.5), JsonPrimitive(50.0),
                JsonPrimitive(0.017), JsonPrimitive(3005.0), JsonPrimitive(500.0),
                JsonPrimitive(3100.0), JsonPrimitive(2900.0)
            ))
        ))
        val tickers = listOf(
            Ticker("tBTCUSD", 45000.0, 1.0, 45010.0, 1.5, 100.0, 0.0022, 45005.0, 1000.0, 46000.0, 44000.0),
            Ticker("tETHUSD", 3000.0, 2.0, 3010.0, 2.5, 50.0, 0.017, 3005.0, 500.0, 3100.0, 2900.0)
        )

        `when`(tickersRepositoryMapper.sanitizeSymbols(symbols)).thenReturn(sanitizedSymbols)
        `when`(tickersRemoteSource.fetchTickers(sanitizedSymbols)).thenReturn(jsonArray)
        `when`(tickersRepositoryMapper.toTickers(jsonArray)).thenReturn(tickers)

        val result = tickersRepository.getTickers(symbols).toList()

        verify(tickersRepositoryMapper).sanitizeSymbols(symbols)
        verify(tickersRemoteSource).fetchTickers(sanitizedSymbols)
        verify(tickersRepositoryMapper).toTickers(jsonArray)

        assertEquals(1, result.size)
        assertEquals(tickers, result[0])
    }

    @Test(expected = IllegalArgumentException::class)
    fun getTickers_throws_exception_on_invalid_JSON() = runTest {
        val symbols = "BTCUSD"
        val sanitizedSymbols = "tBTCUSD"
        val invalidJsonArray = JsonArray(listOf(JsonPrimitive("invalid")))

        `when`(tickersRepositoryMapper.sanitizeSymbols(symbols)).thenReturn(sanitizedSymbols)
        `when`(tickersRemoteSource.fetchTickers(sanitizedSymbols)).thenReturn(invalidJsonArray)
        `when`(tickersRepositoryMapper.toTickers(invalidJsonArray)).thenThrow(IllegalArgumentException::class.java)

        tickersRepository.getTickers(symbols).collect()
    }
}
