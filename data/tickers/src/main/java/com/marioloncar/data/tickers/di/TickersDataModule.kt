package com.marioloncar.data.tickers.di

import com.marioloncar.data.tickers.data.TickersRepositoryImpl
import com.marioloncar.data.tickers.data.mapper.TickersRepositoryMapper
import com.marioloncar.data.tickers.data.mapper.TickersRepositoryMapperImpl
import com.marioloncar.data.tickers.data.source.remote.TickersRemoteSource
import com.marioloncar.data.tickers.data.source.remote.TickersRemoteSourceImpl
import com.marioloncar.data.tickers.domain.TickersRepository
import com.marioloncar.data.tickers.domain.usecase.GetLiveTickersUseCase
import com.marioloncar.data.tickers.domain.usecase.GetTickersUseCase
import org.koin.dsl.module

val tickersDataModule = module {

    single<TickersRemoteSource> { TickersRemoteSourceImpl(httpClient = get()) }
    single<TickersRepositoryMapper> { TickersRepositoryMapperImpl() }
    single<TickersRepository> {
        TickersRepositoryImpl(
            tickersRemoteSource = get(),
            tickersRepositoryMapper = get()
        )
    }

    single { GetTickersUseCase(tickersRepository = get()) }
    single { GetLiveTickersUseCase(getTickersUseCase = get(), tickerUtil = get()) }
}
