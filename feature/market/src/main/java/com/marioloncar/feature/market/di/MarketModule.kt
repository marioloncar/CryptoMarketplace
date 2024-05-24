package com.marioloncar.feature.market.di

import com.marioloncar.feature.market.presentation.MarketViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val marketModule = module {

    viewModelOf(::MarketViewModel)
}
