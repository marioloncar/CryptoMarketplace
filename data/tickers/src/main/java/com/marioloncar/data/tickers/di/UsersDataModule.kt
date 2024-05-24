package com.marioloncar.data.tickers.di

import com.marioloncar.data.tickers.data.TickersRepositoryImpl
import com.marioloncar.data.tickers.data.source.remote.TickersRemoteSource
import com.marioloncar.data.tickers.data.source.remote.TickersRemoteSourceImpl
import com.marioloncar.data.tickers.domain.TickersRepository
import com.marioloncar.data.tickers.domain.usecase.GetTickersUseCase
import io.ktor.client.HttpClient
import org.koin.dsl.module

val tickersDataModule = module {

    single<TickersRemoteSource> { TickersRemoteSourceImpl(httpClient = get()) }
    single<TickersRepository> { TickersRepositoryImpl(tickersRemoteSource = get(), json = get()) }

    single { GetTickersUseCase(tickersRepository = get()) }
}
